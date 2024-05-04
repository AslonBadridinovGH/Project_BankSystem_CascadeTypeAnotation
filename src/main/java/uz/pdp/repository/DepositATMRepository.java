package uz.pdp.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.entity.DepositInATM;

import java.util.List;


@Repository
public interface DepositATMRepository extends JpaRepository<DepositInATM, Integer> {

    List<DepositInATM> findAllByAtmId(Integer atm_id);



}
