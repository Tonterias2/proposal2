package com.spingular.proposal.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A VoteProposal.
 */
@Entity
@Table(name = "vote_proposal")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VoteProposal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @NotNull
    @Column(name = "vote_points", nullable = false)
    private Long votePoints;

    @ManyToOne
    @JsonIgnoreProperties("voteProposals")
    private Proposal proposal;

    @ManyToOne
    @JsonIgnoreProperties("voteProposals")
    private ProposalUser proposalUser;

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

    public VoteProposal creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Long getVotePoints() {
        return votePoints;
    }

    public VoteProposal votePoints(Long votePoints) {
        this.votePoints = votePoints;
        return this;
    }

    public void setVotePoints(Long votePoints) {
        this.votePoints = votePoints;
    }

    public Proposal getProposal() {
        return proposal;
    }

    public VoteProposal proposal(Proposal proposal) {
        this.proposal = proposal;
        return this;
    }

    public void setProposal(Proposal proposal) {
        this.proposal = proposal;
    }

    public ProposalUser getProposalUser() {
        return proposalUser;
    }

    public VoteProposal proposalUser(ProposalUser proposalUser) {
        this.proposalUser = proposalUser;
        return this;
    }

    public void setProposalUser(ProposalUser proposalUser) {
        this.proposalUser = proposalUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VoteProposal)) {
            return false;
        }
        return id != null && id.equals(((VoteProposal) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "VoteProposal{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", votePoints=" + getVotePoints() +
            "}";
    }
}
