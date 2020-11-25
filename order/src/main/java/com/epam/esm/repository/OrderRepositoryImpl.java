package com.epam.esm.repository;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Pagination;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository("orderRepository")
public class OrderRepositoryImpl implements OrderRepository {
    private static final String USER_ORDERS = "SELECT o FROM Order o WHERE o.userId=?1";
    private static final String ORDER_INFO = "SELECT o FROM Order o WHERE o.userId = ?1 AND o.id = ?2";
    private static final String ADD_CERTIFICATE_TO_ORDER = "INSERT INTO `gift-certificates`." +
            "`orders_m2m_gift_certificate` VALUES(?,?)";
    private static final String USER_NAME = "SELECT `name` FROM `gift-certificates`.users WHERE id=?";
    @PersistenceContext
    private EntityManager  entityManager;

    @Override
    public Optional<Order> getOrderInfo(long userId, long orderId) {
        Order order =  entityManager.createQuery(ORDER_INFO, Order.class)
                .setParameter(1, userId)
                .setParameter(2, orderId)
                .getResultList()
                .stream()
                .findFirst().orElse(null);
        return Optional.ofNullable(order);
    }

    @Override
    public List<Order> getUserOrders(long userId, Pagination pagination) {
        return entityManager.createQuery(USER_ORDERS, Order.class)
                .setParameter(1, userId)
                .setFirstResult(pagination.getOffset())
                .setMaxResults(pagination.getLimit())
                .getResultList();
    }

    @Override
    public Optional<Order> order(Order order, List<Certificate> certificates) {
        long createdOrderId = createOrder(order);

        getOrderInfo(order.getUserId(), createdOrderId).ifPresent(
                order1-> addCertificatesToOrder(certificates, createdOrderId)
        );
        return getOrderInfo(order.getUserId(), createdOrderId);
    }

    @Override
    public boolean doesUserExist(long userId) {
        try {
            String name = (String) entityManager.createNativeQuery(USER_NAME)
                    .setParameter(1, userId)
                    .getSingleResult();
        }catch (NoResultException e){
            return false;
        }
        return true;
    }

    private long createOrder(Order order){
        entityManager.persist(order);
        return order.getId();
    }

    private void addCertificatesToOrder(List<Certificate> certificates, long orderId){
        for (Certificate certificate: certificates) {
            entityManager.createNativeQuery(ADD_CERTIFICATE_TO_ORDER)
                    .setParameter(1, orderId)
                    .setParameter(2, certificate.getId())
                    .executeUpdate();
        }
    }
}
