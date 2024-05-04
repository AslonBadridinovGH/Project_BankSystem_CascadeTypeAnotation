package uz.pdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.entity.enumClass.BankNote;
import uz.pdp.entity.enums.BankNoteName;

public interface BanknoteRepository extends JpaRepository<BankNote,Integer> {

    boolean existsByBankNoteName(BankNoteName banknoteName);

    BankNote findByBankNoteName(BankNoteName bankNoteName);
}
