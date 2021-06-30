package com.crm.servicebackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "service_center_id")
    private ServiceCenter serviceCenter;
    @Column(name = "client_name")
    private String clientName;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(nullable = false)
    private String problem;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "type_id")
    private Type type;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "model_id")
    private Model model;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "accepted_user_id")
    private User acceptedUser;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "done_user_id")
    private User doneUser;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "give_user_id")
    private User giveUser;
    @Column(name = "accepted_date")
    private Date acceptedDate;
    @Column(name = "gave_date")
    private Date gaveDate;
    @Column(name = "done_date")
    private Date doneDate;
    private int price;
    private Status status;
    private TypesOfPayments typesOfPayments;
    private Boolean notified;
    private String comment;
    private String modelCompany;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "discount_id")
    private Discount discount;
    @Column(nullable = false)
    private String discountName;
    @Column(nullable = false)
    private int discountPercent;
    @Column(nullable = false)
    private String token;

    public Order(String clientName, String phoneNumber, String problem, User acceptedUser, Type type, Model model, String modelCompany) {
        this.serviceCenter = serviceCenter;
        this.clientName = clientName;
        this.phoneNumber = phoneNumber;
        this.problem = problem;
        this.acceptedUser = acceptedUser;
        this.acceptedDate = new Date();
        this.status = Status.NOTDONE;
        this.type = type;
        this.model = model;
        this.modelCompany = modelCompany;
    }

    public Order(String clientName, String phoneNumber, String problem, User acceptedUser, Type type, Model model, String modelCompany, String discountName, int discountPercent) {
        this.serviceCenter = serviceCenter;
        this.clientName = clientName;
        this.phoneNumber = phoneNumber;
        this.problem = problem;
        this.acceptedUser = acceptedUser;
        this.acceptedDate = new Date();
        this.status = Status.NOTDONE;
        this.type = type;
        this.model = model;
        this.modelCompany = modelCompany;
        this.discountName = discountName;
        this.discountPercent = discountPercent;
    }

    public Order(Client client, String problem, User acceptedUser, Type type, Model model, String modelCompany) {
        this.problem = problem;
        this.acceptedUser = acceptedUser;
        this.acceptedDate = new Date();
        this.status = Status.NOTDONE;
        this.type = type;
        this.model = model;
        this.modelCompany = modelCompany;
        this.discountName = client.getDiscount().getDiscountName();
        this.discountPercent = client.getDiscount().getPercentage();
        this.client = client;
    }


    public void addOrderItem(OrderItem orderItem){
        this.items.add(orderItem);
        if(orderItem.getProduct()!=null) {
            this.price += orderItem.getQuantity() * orderItem.getProduct().getPrice();
        }
        else {
            this.price+=orderItem.getQuantity()*orderItem.getService().getPrice();
        }
    }

    public OrderItem getOrderItemById(long id){
        for(OrderItem orderItem: items){
            if(orderItem.getId()==id){
                return orderItem;
            }
        }
        return null;
    }

    public void clearItems() {
        this.items.clear();
    }

    public void removeOrderItemById(long id){
        for(OrderItem orderItem: items){
            if(orderItem.getId()==id){
                if(orderItem.getProduct()!=null) {
                    this.price -= orderItem.getQuantity() * orderItem.getSoldPrice();
                    items.remove(orderItem);
                }
                else {
                    this.price-=orderItem.getQuantity()*orderItem.getSoldPrice();
                    items.remove(orderItem);
                }
                break;
            }
        }
    }

    public void deleteProductFromItems(Product product){
        for(OrderItem orderItem: items){
            if(orderItem.getProduct()!=null&&orderItem.getProduct().getId()==product.getId()){
                items.remove(orderItem);
            }
        }
    }
}
