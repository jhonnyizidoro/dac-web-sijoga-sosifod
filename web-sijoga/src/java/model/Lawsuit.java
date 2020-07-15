package model;

import interfaces.LawsuitCustomMethods;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="lawsuits")
public class Lawsuit implements Serializable, LawsuitCustomMethods {
    private int id;
    private int status;
    private String note;
    private Date createdAt;
    private User promoted;
    private User promoting;
    private User promotedLawier;
    private User promotingLawier;
    private User judge;
    private List<Phase> phases;

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Column(name="created_at")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @ManyToOne
    @JoinColumn(name="promoted_id")
    public User getPromoted() {
        return promoted;
    }

    public void setPromoted(User promoted) {
        this.promoted = promoted;
    }

    @ManyToOne
    @JoinColumn(name="promoting_id")
    public User getPromoting() {
        return promoting;
    }

    public void setPromoting(User promoting) {
        this.promoting = promoting;
    }

    @ManyToOne
    @JoinColumn(name="promoted_lawier_id")
    public User getPromotedLawier() {
        return promotedLawier;
    }

    public void setPromotedLawier(User promotedLawier) {
        this.promotedLawier = promotedLawier;
    }

    @ManyToOne
    @JoinColumn(name="promoting_lawier_id")
    public User getPromotingLawier() {
        return promotingLawier;
    }

    public void setPromotingLawier(User promotingLawier) {
        this.promotingLawier = promotingLawier;
    }

    @ManyToOne
    @JoinColumn(name="judge_id")
    public User getJudge() {
        return judge;
    }

    public void setJudge(User judge) {
        this.judge = judge;
    }

    @OneToMany(mappedBy="lawsuit")
    public List<Phase> getPhases() {
        return phases;
    }

    public void setPhases(List<Phase> phases) {
        this.phases = phases;
    }

    @Override
    public Phase lastPhase() {
        if (this.phases.size() > 0) {
            return this.phases.get(this.phases.size() - 1);
        }
        return null;
    }
    
    @Override
    public String currentStatus() {
        if (this.status != 0) {
            return "Finalizado";
        } else if (this.phases.isEmpty()) {
            return "Novo";
        } else {
            if (this.phases.get(this.phases.size() - 1).getType() == 2) {
                return "Aguardando movimentação do Juiz";
            }
            return "Em aberto";
        }
    }
    
}
