package uz.pdp.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.entity.Atm;

//@RepositoryRestResource(path = "card",excerptProjection = CardProjection.class)
public interface BankATMRepository extends JpaRepository<Atm, Integer> {

}
