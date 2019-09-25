package deck.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "USERS")
public class User implements UserDetails {

	private long id;

	@JsonIgnore
	private String emailId;

	@JsonIgnore
	private String password;

	private Integer deckCount;

	@Transient
	private Collection<GrantedAuthority> authorities;

	@Convert(converter = AuthorityConverter.class)
	private GrantedAuthority authority;

	private boolean active;

	private Set<Deck> decks;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "email_id", nullable = false)
	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	@JsonIgnore
	@Override
	@Transient
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public String getAuthority() {
		return new AuthorityConverter().convertToDatabaseColumn(authority);
	}

	@JsonIgnore
	@Transient
	public GrantedAuthority getAuthorityEntity() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = new AuthorityConverter().convertToEntityAttribute(authority);
	}

	public String getPassword() {
		return password;
	}

	@Transient
	@Override
	public String getUsername() {
		return emailId;
	}

	@JsonIgnore
	@Transient
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Transient
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Transient
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Transient
	@Override
	public boolean isEnabled() {
		return true;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", emailId='" + emailId + '\'' +
				", authorities=" + authority +
				", active=" + active +
				", decks=" + decks +
				'}';
	}

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = { CascadeType.ALL })
	public Set<Deck> getDecks() {
		return decks;
	}

	@Transient
	public Integer getDeckCount() {
		return deckCount;
	}

	public void setDeckCount(Integer deckCount) {
		this.deckCount = deckCount;
	}

	public void setDecks(Set<Deck> decks) {
		this.decks = decks;
	}
}
