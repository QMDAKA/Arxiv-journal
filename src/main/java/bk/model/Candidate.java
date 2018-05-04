package bk.model;

import javax.persistence.*;

/**
 * Created by quangminh on 14/09/2017.
 */
@Entity
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    Long id;
    double CN;
    double AA;
    double JC;
    double WCN;
    double WAA;
    double WJC;
    double EWCN;
    double EWAA;
    double EWJC;
    String authorId1;
    String authorId2;
    boolean label;
    boolean predictByNormalMeasure;
    boolean predictByWeightedMeasure;

    double weight;
    int yearEnd;
    int yearStart;

    public boolean isPredictByNormalMeasure() {
        return predictByNormalMeasure;
    }

    public void setPredictByNormalMeasure(boolean predictByNormalMeasure) {
        this.predictByNormalMeasure = predictByNormalMeasure;
    }

    public boolean isPredictByWeightedMeasure() {
        return predictByWeightedMeasure;
    }

    public void setPredictByWeightedMeasure(boolean predictByWeightedMeasure) {
        this.predictByWeightedMeasure = predictByWeightedMeasure;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getCN() {
        return CN;
    }

    public void setCN(double CN) {
        this.CN = CN;
    }

    public double getAA() {
        return AA;
    }

    public void setAA(double AA) {
        this.AA = AA;
    }

    public double getJC() {
        return JC;
    }

    public void setJC(double JC) {
        this.JC = JC;
    }

    public double getWCN() {
        return WCN;
    }

    public void setWCN(double WCN) {
        this.WCN = WCN;
    }

    public double getWAA() {
        return WAA;
    }

    public void setWAA(double WAA) {
        this.WAA = WAA;
    }

    public double getWJC() {
        return WJC;
    }

    public void setWJC(double WJC) {
        this.WJC = WJC;
    }

    public double getEWCN() {
        return EWCN;
    }

    public void setEWCN(double EWCN) {
        this.EWCN = EWCN;
    }

    public double getEWAA() {
        return EWAA;
    }

    public void setEWAA(double EWAA) {
        this.EWAA = EWAA;
    }

    public double getEWJC() {
        return EWJC;
    }

    public void setEWJC(double EWJC) {
        this.EWJC = EWJC;
    }

    public String getAuthorId1() {
        return authorId1;
    }

    public void setAuthorId1(String authorId1) {
        this.authorId1 = authorId1;
    }

    public String getAuthorId2() {
        return authorId2;
    }

    public void setAuthorId2(String authorId2) {
        this.authorId2 = authorId2;
    }

    public boolean isLabel() {
        return label;
    }

    public void setLabel(boolean label) {
        this.label = label;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getYearEnd() {
        return yearEnd;
    }

    public void setYearEnd(int yearEnd) {
        this.yearEnd = yearEnd;
    }

    public int getYearStart() {
        return yearStart;
    }

    public void setYearStart(int yearStart) {
        this.yearStart = yearStart;
    }
}
