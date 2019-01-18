package lk.ijse.dep.app.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO extends SuperDTO{

    private String id;
    private LocalDate date;
    private String customerId;
    private List<OrderDetailDTO> orderDetailDTOS = new ArrayList<>();

    public OrderDTO() {
    }

    public OrderDTO(String id, LocalDate date, String customerId, List<OrderDetailDTO> orderDetailDTOS) {
        this.id = id;
        this.date = date;
        this.customerId = customerId;
        this.orderDetailDTOS = orderDetailDTOS;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<OrderDetailDTO> getOrderDetailDTOS() {
        return orderDetailDTOS;
    }

    public void setOrderDetailDTOS(List<OrderDetailDTO> orderDetailDTOS) {
        this.orderDetailDTOS = orderDetailDTOS;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", customerId='" + customerId + '\'' +
                ", orderDetailDTOS=" + orderDetailDTOS +
                '}';
    }
}
