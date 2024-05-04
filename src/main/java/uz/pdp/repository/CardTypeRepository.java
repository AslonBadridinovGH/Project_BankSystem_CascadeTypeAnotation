package uz.pdp.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.entity.enumClass.CardType;
import uz.pdp.entity.enums.CardTypeName;

public interface CardTypeRepository extends JpaRepository<CardType,Integer> {
      CardType findByCardTypeName(CardTypeName roleName);
}
