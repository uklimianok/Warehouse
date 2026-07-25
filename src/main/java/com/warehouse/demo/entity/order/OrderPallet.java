package com.warehouse.demo.entity.order;

import com.warehouse.demo.entity.item.Pallet;
import com.warehouse.demo.entity.service.Status;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_pallets")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderPallet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_pallets_seq")
    @SequenceGenerator(name = "order_pallets_seq", sequenceName = "order_pallets_seq", allocationSize = 50)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pallet_id", referencedColumnName = "id")
    private Pallet pallet;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;
}
