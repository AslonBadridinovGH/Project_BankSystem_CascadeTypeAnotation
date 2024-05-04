package uz.pdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.entity.WithdrawFromCard;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface WithdrawMoneyRepository extends JpaRepository<WithdrawFromCard, Integer> {

      List<WithdrawFromCard> findAllByAtmIdAndDate(Integer atm_id, LocalDate date);

      List<WithdrawFromCard> findAllByAtmId(Integer atm_id);

}
