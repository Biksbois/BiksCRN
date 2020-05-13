package simpleAdder.interpret.Objects.ProtocolOBJ;

public class Equilibrate {
    public String sample;
    public String amount;
    public float stepSize = 0.00125f;
    public String timeInterval = "";

    public Equilibrate(String sample, String amount)
    {
        this.sample = sample;
        this.amount = amount;
    }

    public Equilibrate(String sample, String amount, String stepSize)
    {
        this.sample = sample;
        this.amount = amount;
        this.stepSize = stepSize.equals("") ? this.stepSize: Float.parseFloat(stepSize);
    }

    public Equilibrate(String sample, String amount, String stepSize, String timeInterval)
    {
        this.sample = sample;
        this.amount = amount;
        this.stepSize = stepSize.equals("") ? this.stepSize: Float.parseFloat(stepSize);
        this.timeInterval = timeInterval;
    }
}
