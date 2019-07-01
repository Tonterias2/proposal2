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
 * A ProposalUser.
 */
@Entity
@Table(name = "proposal_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProposalUser implements Serializable {

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
//    private Long assignedVotesPoints = 100L;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "proposalUser")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Proposal> proposals = new HashSet<>();

    @OneToMany(mappedBy = "proposalUser")
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

    public ProposalUser creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Long getAssignedVotesPoints() {
        return assignedVotesPoints;
    }

    public ProposalUser assignedVotesPoints(Long assignedVotesPoints) {
        this.assignedVotesPoints = assignedVotesPoints;
        return this;
    }

    public void setAssignedVotesPoints(Long assignedVotesPoints) {
        this.assignedVotesPoints = assignedVotesPoints;
    }

    public User getUser() {
        return user;
    }

    public ProposalUser user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Proposal> getProposals() {
        return proposals;
    }

    public ProposalUser proposals(Set<Proposal> proposals) {
        this.proposals = proposals;
        return this;
    }

    public ProposalUser addProposal(Proposal proposal) {
        this.proposals.add(proposal);
        proposal.setProposalUser(this);
        return this;
    }

    public ProposalUser removeProposal(Proposal proposal) {
        this.proposals.remove(proposal);
        proposal.setProposalUser(null);
        return this;
    }

    public void setProposals(Set<Proposal> proposals) {
        this.proposals = proposals;
    }

    public Set<VoteProposal> getVoteProposals() {
        return voteProposals;
    }

    public ProposalUser voteProposals(Set<VoteProposal> voteProposals) {
        this.voteProposals = voteProposals;
        return this;
    }

    public ProposalUser addVoteProposal(VoteProposal voteProposal) {
        this.voteProposals.add(voteProposal);
        voteProposal.setProposalUser(this);
        return this;
    }

    public ProposalUser removeVoteProposal(VoteProposal voteProposal) {
        this.voteProposals.remove(voteProposal);
        voteProposal.setProposalUser(null);
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
        if (!(o instanceof ProposalUser)) {
            return false;
        }
        return id != null && id.equals(((ProposalUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ProposalUser{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", assignedVotesPoints=" + getAssignedVotesPoints() +
            "}";
    }
}
