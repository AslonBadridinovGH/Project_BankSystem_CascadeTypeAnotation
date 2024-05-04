package uz.pdp.security;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import uz.pdp.entity.enumClass.Role;


import java.util.Date;
import java.util.Set;

@Component
public class JwtProvider {


    static long expireTime = 36_000_000;
    static String keyword = "buTokenniMaxfiySoziCLIENTBILMASIN!!!";


    public static String generateToken(String username,Set<Role> roles) {
        Date expireDate = new Date(System.currentTimeMillis() + expireTime);
        String token = Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, keyword)
                .claim("roles",roles)
                .compact();
        return token;
    }

    // RECEIVING THE EMAIL FROM THE TOKEN WHEN THE CLIENT LOGIN WITH THE TOKEN FOR THE 2ND TIME
    public String getEmailFromToken(String token){

        try {
            String email = Jwts
                    .parser()
                    .setSigningKey(keyword)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return email;

        }catch (Exception e){
            return null;
        }
    }


}