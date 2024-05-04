package uz.pdp.entity.enumClass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import uz.pdp.entity.enums.CardTypeName;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CardType implements GrantedAuthority{

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;


        @Enumerated(EnumType.STRING)
        private CardTypeName cardTypeName;

        @Override
        public String getAuthority() {
            return cardTypeName.name();
        }

    }
