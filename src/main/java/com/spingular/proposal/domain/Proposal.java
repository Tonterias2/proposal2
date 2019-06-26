package com.spingular.proposal.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.spingular.proposal.domain.enumeration.ProposalType;

import com.spingular.proposal.domain.enumeration.ProposalRole;

/**
 * A Proposal.
 */
@Entity
@Table(name = "proposal")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Proposal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @NotNull
    @Size(min = 2, max = 250)
    @Column(name = "proposal_name", length = 250, nullable = false)
    private String proposalName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "proposal_type", nullable = false)
    private ProposalType proposalType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "proposal_role", nullable = false)
    private ProposalRole proposalRole;

    @Column(name = "release_date")
    private Instant releaseDate;

//    @Column(name = "proposal_votes")
    @Transient
    private Long proposalVotes;

//    @Column(name = "proposal_user_votes")
    @Transient
    private Long proposalUserVotes;

    @OneToMany(mappedBy = "proposal")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<VoteProposal> voteProposals = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("proposals")
    private ProposalUser proposalUser;

    @ManyToOne
    @JsonIgnoreProperties("proposals")
    private Post post;

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

    public Proposal creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getProposalName() {
        return proposalName;
    }

    public Proposal proposalName(String proposalName) {
        this.proposalName = proposalName;
        return this;
    }

    public void setProposalName(String proposalName) {
        this.proposalName = proposalName;
    }

    public ProposalType getProposalType() {
        return proposalType;
    }

    public Proposal proposalType(ProposalType proposalType) {
        this.proposalType = proposalType;
        return this;
    }

    public void setProposalType(ProposalType proposalType) {
        this.proposalType = proposalType;
    }

    public ProposalRole getProposalRole() {
        return proposalRole;
    }

    public Proposal proposalRole(ProposalRole proposalRole) {
        this.proposalRole = proposalRole;
        return this;
    }

    public void setProposalRole(ProposalRole proposalRole) {
        this.proposalRole = proposalRole;
    }

    public Instant getReleaseDate() {
        return releaseDate;
    }

    public Proposal releaseDate(Instant releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public void setReleaseDate(Instant releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Long getProposalVotes() {
        return proposalVotes;
    }

    public Proposal proposalVotes(Long proposalVotes) {
        this.proposalVotes = proposalVotes;
        return this;
    }

    public void setProposalVotes(Long proposalVotes) {
        this.proposalVotes = proposalVotes;
    }

    public Long getProposalUserVotes() {
        return proposalUserVotes;
    }

    public Proposal proposalUserVotes(Long proposalUserVotes) {
        this.proposalUserVotes = proposalUserVotes;
        return this;
    }

    public void setProposalUserVotes(Long proposalUserVotes) {
        this.proposalUserVotes = proposalUserVotes;
    }

    public Set<VoteProposal> getVoteProposals() {
        return voteProposals;
    }

    public Proposal voteProposals(Set<VoteProposal> voteProposals) {
        this.voteProposals = voteProposals;
        return this;
    }

    public Proposal addVoteProposal(VoteProposal voteProposal) {
        this.voteProposals.add(voteProposal);
        voteProposal.setProposal(this);
        return this;
    }

    public Proposal removeVoteProposal(VoteProposal voteProposal) {
        this.voteProposals.remove(voteProposal);
        voteProposal.setProposal(null);
        return this;
    }

    public void setVoteProposals(Set<VoteProposal> voteProposals) {
        this.voteProposals = voteProposals;
    }

    public ProposalUser getProposalUser() {
        return proposalUser;
    }

    public Proposal proposalUser(ProposalUser proposalUser) {
        this.proposalUser = proposalUser;
        return this;
    }

    public void setProposalUser(ProposalUser proposalUser) {
        this.proposalUser = proposalUser;
    }

    public Post getPost() {
        return post;
    }

    public Proposal post(Post post) {
        this.post = post;
        return this;
    }

    public void setPost(Post post) {
        this.post = post;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Proposal)) {
            return false;
        }
        return id != null && id.equals(((Proposal) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Proposal{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", proposalName='" + getProposalName() + "'" +
            ", proposalType='" + getProposalType() + "'" +
            ", proposalRole='" + getProposalRole() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", proposalVotes=" + getProposalVotes() +
            ", proposalUserVotes=" + getProposalUserVotes() +
            "}";
    }
}
