package com.spingular.proposal.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Profile.
 */
@Entity
@Table(name = "profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "assigned_votes_points")
    private Long assignedVotesPoints;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "profile")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Proposal> proposals = new HashSet<>();

    @OneToMany(mappedBy = "profile")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<VoteProposal> voteProposals = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public Profile creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Long getAssignedVotesPoints() {
        return assignedVotesPoints;
    }

    public Profile assignedVotesPoints(Long assignedVotesPoints) {
        this.assignedVotesPoints = assignedVotesPoints;
        return this;
    }

    public void setAssignedVotesPoints(Long assignedVotesPoints) {
    	assignedVotesPoints = 100L;
        this.assignedVotesPoints = assignedVotesPoints;
    }

    public User getUser() {
        return user;
    }

    public Profile user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Proposal> getProposals() {
        return proposals;
    }

    public Profile proposals(Set<Proposal> proposals) {
        this.proposals = proposals;
        return this;
    }

    public Profile addProposal(Proposal proposal) {
        this.proposals.add(proposal);
        proposal.setProfile(this);
        return this;
    }

    public Profile removeProposal(Proposal proposal) {
        this.proposals.remove(proposal);
        proposal.setProfile(null);
        return this;
    }

    public void setProposals(Set<Proposal> proposals) {
        this.proposals = proposals;
    }

    public Set<VoteProposal> getVoteProposals() {
        return voteProposals;
    }

    public Profile voteProposals(Set<VoteProposal> voteProposals) {
        this.voteProposals = voteProposals;
        return this;
    }

    public Profile addVoteProposal(VoteProposal voteProposal) {
        this.voteProposals.add(voteProposal);
        voteProposal.setProfile(this);
        return this;
    }

    public Profile removeVoteProposal(VoteProposal voteProposal) {
        this.voteProposals.remove(voteProposal);
        voteProposal.setProfile(null);
        return this;
    }

    public void setVoteProposals(Set<VoteProposal> voteProposals) {
        this.voteProposals = voteProposals;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Profile)) {
            return false;
        }
        return id != null && id.equals(((Profile) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Profile{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", assignedVotesPoints=" + getAssignedVotesPoints() +
            "}";
    }
}
