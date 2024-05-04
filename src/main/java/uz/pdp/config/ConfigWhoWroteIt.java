package uz.pdp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing   // Jpa Enable auditing
                     // A CLASS THAT RETURNS WHO WRITTEN THE CRUD OPERATION OF Product
public class ConfigWhoWroteIt {

      @Bean
      AuditorAware<Integer>auditorAware(){
         return new WhoWroteIt();
      }

}
