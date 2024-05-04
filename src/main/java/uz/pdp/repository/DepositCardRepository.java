package uz.pdp.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.entity.DepositInCard;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface DepositCardRepository extends JpaRepository<DepositInCard, Integer> {


    List<DepositInCard> findAllByAtmId(Integer atm_id);

    List<DepositInCard> findAllByAtmIdAndDate(Integer atm_id, LocalDate date);

}
