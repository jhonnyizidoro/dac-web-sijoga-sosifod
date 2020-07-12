package mb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import model.Lawsuit;
import model.Phase;

@Named
@RequestScoped
public class LawsuitMB {
    private Lawsuit lawsuit;
    private Phase phase = new Phase();

    public Lawsuit getLawsuit() {
        return lawsuit;
    }

    public void setLawsuit(Lawsuit lawsuit) {
        this.lawsuit = lawsuit;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public String show(Lawsuit lawsuit) {
        this.lawsuit = lawsuit;
        return "lawsuit";
    }
    
}
