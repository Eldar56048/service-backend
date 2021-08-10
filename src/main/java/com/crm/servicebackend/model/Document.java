package com.crm.servicebackend.model;

import com.crm.servicebackend.model.enums.DocumentStatus;
import com.crm.servicebackend.model.enums.DocumentType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;
    @Column(nullable = false)
    private Date created;
    private Date printed;
    private Date signed;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    private DocumentType documentType;
    private DocumentStatus documentStatus;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_center_id", nullable = false)
    private ServiceCenter serviceCenter;

    public Document(Date created, Order order, DocumentType documentType, DocumentStatus documentStatus, ServiceCenter serviceCenter) {
        this.created = created;
        this.order = order;
        this.documentType = documentType;
        this.documentStatus = documentStatus;
        this.serviceCenter = serviceCenter;
    }
}

