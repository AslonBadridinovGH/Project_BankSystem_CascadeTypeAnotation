package uz.pdp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.pdp.entity.enumClass.CardType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)

public class BankCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer  id;

    @Column(unique = true,nullable = false)
    private  String  firstnameCustomer;

    private  String  lastnameCustomer;

    private  Double  balanceInSum;

    private  Double  balanceInDollar;


    @Column(unique = true, nullable = false, length = 16)
    private  String  number16;

    @Column(unique = true, nullable = false, length = 3)
    private  String  CVVCode3;

    @Column(unique = true, nullable = false, length = 4)
    private  String  parole4;



    private  LocalDate  validityPeriod;

    @OneToOne(cascade = CascadeType.ALL)
    private CardType cardType;

    private  boolean   enabled;

    @ManyToOne(cascade = CascadeType.ALL)
    private Banks bankName;


    @CreatedBy
    private Integer createdBy;

    @LastModifiedBy
    private Integer updatedBy;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

}

