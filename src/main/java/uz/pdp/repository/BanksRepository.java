package uz.pdp.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.entity.Banks;


//@RepositoryRestResource(path = "card",excerptProjection = CardProjection.class)
public interface BanksRepository extends JpaRepository<Banks, Integer> {

}
