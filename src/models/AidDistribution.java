/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author AL
 */
@Entity
@Table(name = "aiddistribution")
public class AidDistribution {

    @Id
    @Column(name = "distribution_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int distributionId;

    @ManyToOne
    @JoinColumn(name = "family_id")
    private Family family;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "distributed_by")
    private User distributedBy;

    @Column(name = "distribution_date")
    private LocalDate distributionDate;

    // constructor
    public AidDistribution() {
    }

    public AidDistribution(int distributionId, Family family, Organization organization, User distributedBy, LocalDate distributionDate) {
        this.distributionId = distributionId;
        this.family = family;
        this.organization = organization;
        this.distributedBy = distributedBy;
        this.distributionDate = distributionDate;
    }

    public int getDistributionId() {
        return distributionId;
    }

    public void setDistributionId(int distributionId) {
        this.distributionId = distributionId;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public User getDistributedBy() {
        return distributedBy;
    }

    public void setDistributedBy(User distributedBy) {
        this.distributedBy = distributedBy;
    }

    public LocalDate getDistributionDate() {
        return distributionDate;
    }

    public void setDistributionDate(LocalDate distributionDate) {
        this.distributionDate = distributionDate;
    }

}
