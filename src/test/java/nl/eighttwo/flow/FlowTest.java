package nl.eighttwo.flow;


import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FlowTest {

    private enum JaNee {
        JA,NEE
    }

    private class OphalenStudent extends Actie<BepalenSoortIetsContext> {
        @Override
        public void voerUit(BepalenSoortIetsContext context) {
            // doe iets met het burgerservicenummer...
            context.setStudent(new Student());
        }
    }

    private class Fout extends Actie<BepalenSoortIetsContext> {
        @Override
        public void voerUit(BepalenSoortIetsContext context) {
            
        }
    }

    private class IsBekendeStudent extends Beslissing<Boolean, BepalenSoortIetsContext> {
        @Override
        public Boolean evalueer(BepalenSoortIetsContext context) {
            return context.getStudent() != null;
        }
    }

    private class IsBekendeOpleiding extends Beslissing<JaNee, BepalenSoortIetsContext> {
        @Override
        public JaNee evalueer(BepalenSoortIetsContext context) {
            return JaNee.JA;
        }
    }

    private class BepalenReedsEerderBehaaldeGraden extends Actie<BepalenSoortIetsContext> {
        @Override
        public void voerUit(BepalenSoortIetsContext context) {
            context.getBehaaldeGraden().add(Graad.AD);
        }
    }

    private class IsIndicatieSoortLG extends Beslissing<JaNee, BepalenSoortIetsContext> {
        @Override
        public JaNee evalueer(BepalenSoortIetsContext context) {
            return JaNee.JA;
        }
    }

    private class BetaalCollegegeld extends Actie<BepalenSoortIetsContext> {
        private SoortCollegegeld soortCollegegeld;

        public BetaalCollegegeld(SoortCollegegeld soortCollegegeld) {
            super();
            this.soortCollegegeld = soortCollegegeld;
        }

        @Override
        public void voerUit(BepalenSoortIetsContext context) {
            context.setSoortCollegegeld(soortCollegegeld);
        }
    }

    private class BepalenSoortIets extends Flow<BepalenSoortIetsContext> {
        private Actie<BepalenSoortIetsContext> ophalenStudent = new OphalenStudent();
        private Beslissing<Boolean, BepalenSoortIetsContext> isBekendeStudent = new IsBekendeStudent();
        private Beslissing<JaNee, BepalenSoortIetsContext> isBekendeOpleiding = new IsBekendeOpleiding();
        private Actie<BepalenSoortIetsContext> bepalenReedsEerderBehaaldeGraden = new BepalenReedsEerderBehaaldeGraden();
        private Beslissing<JaNee, BepalenSoortIetsContext> isIndicatieSectorLG = new IsIndicatieSoortLG();

        private Actie<BepalenSoortIetsContext> betaalWettelijkCollegeld = new BetaalCollegegeld(SoortCollegegeld.W);
        private Actie<BepalenSoortIetsContext> betaalInstellingscollegegeld = new BetaalCollegegeld(SoortCollegegeld.I);

        private Actie<BepalenSoortIetsContext> fout = new Fout();

        private Actie<BepalenSoortIetsContext> etcetera = new Fout();

        public BepalenSoortIets(BepalenSoortIetsContext context) {
            super(context);
        }

        @Override
        Actie<BepalenSoortIetsContext> initieel() {

            ophalenStudent.en(isBekendeStudent);

            // met boolean
            isBekendeStudent.als(Boolean.TRUE).dan(isBekendeOpleiding);
            isBekendeStudent.als(Boolean.FALSE).dan(fout);

            // of als enum
            isBekendeOpleiding.als(JaNee.JA).dan(bepalenReedsEerderBehaaldeGraden);
            isBekendeOpleiding.als(JaNee.NEE).dan(fout);

            bepalenReedsEerderBehaaldeGraden.en(isIndicatieSectorLG);

            isIndicatieSectorLG.als(JaNee.JA).dan(betaalWettelijkCollegeld);
            isIndicatieSectorLG.als(JaNee.NEE).dan(etcetera);

            etcetera.en(betaalInstellingscollegegeld);

            return ophalenStudent;
        }
    }

    private class Student {
        
    }

    private enum Graad {
        BA, AD, MA
    }
    
    private class Opleiding {
        private Graad graad = Graad.AD;

        public boolean isIndicatieBachelor() {
            return true;
        }

        public boolean isIndicatieSoortLG() {
            return true;
        }

    }

    private enum SoortCollegegeld {
        W, I
    }

    private class BepalenSoortIetsContext implements Serializable {
        private String burgerservicenummer;
        private Student student;
        private Opleiding bevraagdeOpleiding;
        private List<Graad> behaaldeGraden = new ArrayList<>();
        private SoortCollegegeld soortCollegegeld;

        public BepalenSoortIetsContext(String burgerservicenummer) {
            this.burgerservicenummer = burgerservicenummer;
        }

        public void setStudent(Student student) {
            this.student = student;
        }

        public Student getStudent() {
            return student;
        }

        public Opleiding getBevraagdeOpleiding() {
            return bevraagdeOpleiding;
        }

        public List<Graad> getBehaaldeGraden() {
            return behaaldeGraden;
        }

        public void setSoortCollegegeld(SoortCollegegeld soortCollegegeld) {
            this.soortCollegegeld = soortCollegegeld;
        }

        public SoortCollegegeld getSoortCollegegeld() {
            return soortCollegegeld;
        }
    }


    @Test
    public void test_flow() {
        BepalenSoortIetsContext bepalenSoortIetsContext = new BepalenSoortIetsContext("19292992");
        Flow<BepalenSoortIetsContext> flow = new BepalenSoortIets(bepalenSoortIetsContext);
        flow.start();

        System.out.println(bepalenSoortIetsContext.getSoortCollegegeld());

    }
}
