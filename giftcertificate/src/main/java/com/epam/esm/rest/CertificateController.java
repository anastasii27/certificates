package com.epam.esm.rest;

import com.epam.esm.dto.CertificateDto;
import static com.epam.esm.model.FilterParam.TAG;
import com.epam.esm.exception.InvalidDataInputException;
import com.epam.esm.model.FilterParam;
import com.epam.esm.model.Pagination;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.utils.UpdatedCertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/certificates")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL_FORMS)
public class CertificateController {
    private final Map<String, Integer> paginationParams = new HashMap<String, Integer>(){{
        put("limit", 0);
        put("offset", 0);
    }};
    @Autowired
    private CertificateService certificateService;
    @Autowired
    private UpdatedCertificateValidator updatedCertificateValidator;

    @GetMapping("/{id}")
    public EntityModel<CertificateDto> getCertificate(@PathVariable long id){
        CertificateDto certificate = certificateService.findById(id);

        return EntityModel.of(certificate, linkTo(methodOn(CertificateController.class)
                .getCertificate(id)).withSelfRel()
                .andAffordance(afford(methodOn(CertificateController.class).updateCertificate(id, null, null)))
                .andAffordance(afford(methodOn(CertificateController.class).deleteCertificate(id))));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCertificate(@PathVariable long id){
        certificateService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<CertificateDto> createCertificate(@RequestBody @Valid CertificateDto certificate){
        CertificateDto createdCertificate = certificateService.create(certificate);

        return EntityModel.of(createdCertificate, linkTo(methodOn(CertificateController.class)
                .createCertificate(certificate)).withSelfRel()
                .andAffordance(afford(methodOn(CertificateController.class).updateCertificate(createdCertificate.getId(),
                        null, null)))
                .andAffordance(afford(methodOn(CertificateController.class).deleteCertificate(
                        createdCertificate.getId()
                ))));
    }

    @PatchMapping("/{id}")
    public EntityModel<CertificateDto> updateCertificate(@PathVariable long id,
                                                         @RequestBody CertificateDto certificate,
                                                         BindingResult bindingResult){
        updatedCertificateValidator.validate(certificate, bindingResult);
        if(bindingResult.hasErrors()){
            throw new InvalidDataInputException(bindingResult);
        }
        CertificateDto updatedCertificate = certificateService.update(certificate, id);

        return EntityModel.of(updatedCertificate, linkTo(methodOn(CertificateController.class)
                .updateCertificate(id, certificate, bindingResult)).withSelfRel()
                .andAffordance(afford(methodOn(CertificateController.class).deleteCertificate(id))));
    }

    @GetMapping
    public List<CertificateDto> getCertificates(@RequestParam Map<String,String> filterParams,
                                             @RequestParam(required = false) Set<String> tag,
                                             @Valid Pagination pagination) {
        List<CertificateDto> certificates;

        if (!filterParams.isEmpty() && !filterParams.keySet().equals(paginationParams.keySet())) {
            putEqualParamsToMap(filterParams, tag, TAG);
            certificates = certificateService.getFilteredCertificates(filterParams, pagination);
        }else {
            certificates = certificateService.findAll(pagination);
        }

        certificates.forEach(
                certificate -> certificate.add(linkTo(methodOn(CertificateController.class)
                        .getCertificate(certificate.getId()))
                        .withSelfRel())
        );
        return certificates;
    }

    private void putEqualParamsToMap(Map<String,String> map, Set<String> equalParams, FilterParam name){
        String paramName = name.name().toLowerCase();
        int i = 0;

        if(equalParams != null) {
            for (String ep : equalParams) {
                map.put(paramName + i++, ep);
            }
        }
        map.remove(paramName);
    }
}

