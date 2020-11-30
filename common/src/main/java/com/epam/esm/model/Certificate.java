package com.epam.esm.model;

import com.epam.esm.converter.ZoneIdConverter;
import com.epam.esm.audit.listener.CertificateListener;
import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(CertificateListener.class)
@Table(name = "certificates")
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDateTime createDate;
    @Convert(converter = ZoneIdConverter.class)
    private ZoneId createDateTimezone;
    private LocalDateTime lastUpdateDate;
    @Convert(converter = ZoneIdConverter.class)
    private ZoneId lastUpdateDateTimezone;
    private long duration;
    @ManyToMany
    @JoinTable(name = "tag_m2m_gift_certificate",
               joinColumns = { @JoinColumn(name = "giftCertificateId")},
               inverseJoinColumns = {@JoinColumn(name = "tagId")})
    private Set<Tag> tags;
    @ManyToMany
    @JoinTable(name = "orders_m2m_gift_certificate",
            joinColumns = { @JoinColumn(name = "giftCertificateId")},
            inverseJoinColumns = {@JoinColumn(name = "ordersId")})
    private Set<Order> orders;

    @Override
    public boolean equals(Object object) {
        if(this == object){
            return true;
        }
        if(object == null){
            return false;
        }
        if(getClass() != object.getClass()){
            return false;
        }

        Certificate that = (Certificate) object;
        if (id != that.id) {
            return false;
        }
        if (duration != that.duration){
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null){
            return false;
        }
        if (description != null ? !description.equals(that.description) : that.description != null) {
            return false;
        }
        if (price != null ? !price.equals(that.price) : that.price != null) {
            return false;
        }
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null){
            return false;
        }
        if (createDateTimezone != null ? !createDateTimezone.equals(that.createDateTimezone)
                : that.createDateTimezone != null){
            return false;
        }

        if (lastUpdateDate != null ? !lastUpdateDate.equals(that.lastUpdateDate) : that.lastUpdateDate != null) {
            return false;
        }
        if (lastUpdateDateTimezone != null ? !lastUpdateDateTimezone.equals(that.lastUpdateDateTimezone)
                : that.lastUpdateDateTimezone != null) {
            return false;
        }
        if (tags != null ? !tags.equals(that.tags) : that.tags != null) {
            return false;
        }
        if (orders != null ? !orders.equals(that.orders) : that.orders != null){
            return false;
        }

        return true;
    }

//    public boolean equals(Object obj){
//        if(this == obj){
//            return true;
//        }
//        if(obj == null){
//            return false;
//        }
//        if(getClass() != obj.getClass()){
//            return false;
//        }

//        Certificate certificate = (Certificate) obj;
//        if(id != certificate.getId()){
//            return false;
//        }
//
//        if(name == null){
//            if(certificate.name != null){
//                return false;
//            }
//        }else if(!name.equals(certificate.name)){
//            return false;
//        }
//
//        return true;
//    }
}
