package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User implements Serializable {
    private int id;
    private String name;
    private String cpf;
    private String password;
    private int profile;
    private List<Lawsuit> promotedLawsuits; //Processos em que foi promovido
    private List<Lawsuit> promotingLawsuits;  //Processos em que foi promovente
    private List<Lawsuit> promotedLawierLawsuits;  //Processos em que foi advogado promovido
    private List<Lawsuit> promotingLawierLawsuits;  //Processos em que foi advogado promovente
    private List<Lawsuit> judgeLawsuits;  //Processos em que foi juiz
    private List<Phase> phases;

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    @OneToMany(mappedBy="promoted")
    public List<Lawsuit> getPromotedLawsuits() {
        return promotedLawsuits;
    }

    public void setPromotedLawsuits(List<Lawsuit> promotedLawsuits) {
        this.promotedLawsuits = promotedLawsuits;
    }

    @OneToMany(mappedBy="promoting")
    public List<Lawsuit> getPromotingLawsuits() {
        return promotingLawsuits;
    }

    public void setPromotingLawsuits(List<Lawsuit> promotingLawsuits) {
        this.promotingLawsuits = promotingLawsuits;
    }

    @OneToMany(mappedBy="promotedLawier")
    public List<Lawsuit> getPromotedLawierLawsuits() {
        return promotedLawierLawsuits;
    }

    public void setPromotedLawierLawsuits(List<Lawsuit> promotedLawierLawsuits) {
        this.promotedLawierLawsuits = promotedLawierLawsuits;
    }

    @OneToMany(mappedBy="promotingLawier")
    public List<Lawsuit> getPromotingLawierLawsuits() {
        return promotingLawierLawsuits;
    }

    public void setPromotingLawierLawsuits(List<Lawsuit> promotingLawierLawsuits) {
        this.promotingLawierLawsuits = promotingLawierLawsuits;
    }

    @OneToMany(mappedBy="judge")
    public List<Lawsuit> getJudgeLawsuits() {
        return judgeLawsuits;
    }

    public void setJudgeLawsuits(List<Lawsuit> judgeLawsuits) {
        this.judgeLawsuits = judgeLawsuits;
    }

    @OneToMany(mappedBy="user")
    public List<Phase> getPhases() {
        return phases;
    }

    public void setPhases(List<Phase> phases) {
        this.phases = phases;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof User)) {
            return false;
        }
        User other = (User) obj;
        return this.id == other.id;
    }
    
}