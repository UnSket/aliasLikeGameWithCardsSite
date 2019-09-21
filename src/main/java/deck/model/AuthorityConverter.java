package deck.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class AuthorityConverter implements AttributeConverter<GrantedAuthority, String> {

    @Override
    public String convertToDatabaseColumn(GrantedAuthority grantedAuthority) {
        return grantedAuthority.getAuthority();
    }

    @Override
    public GrantedAuthority convertToEntityAttribute(String s) {
        return new SimpleGrantedAuthority(s);
    }
}
