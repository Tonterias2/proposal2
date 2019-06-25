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
 * A Post.
 */
@Entity
@Table(name = "post")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Post implements Serializable {

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
    @Column(name = "post_name", length = 250, nullable = false)
    private String postName;

    @OneToMany(mappedBy = "post")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Proposal> proposals = new HashSet<>();

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

    public Post creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getPostName() {
        return postName;
    }

    public Post postName(String postName) {
        this.postName = postName;
        return this;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public Set<Proposal> getProposals() {
        return proposals;
    }

    public Post proposals(Set<Proposal> proposals) {
        this.proposals = proposals;
        return this;
    }

    public Post addProposal(Proposal proposal) {
        this.proposals.add(proposal);
        proposal.setPost(this);
        return this;
    }

    public Post removeProposal(Proposal proposal) {
        this.proposals.remove(proposal);
        proposal.setPost(null);
        return this;
    }

    public void setProposals(Set<Proposal> proposals) {
        this.proposals = proposals;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Post)) {
            return false;
        }
        return id != null && id.equals(((Post) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Post{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", postName='" + getPostName() + "'" +
            "}";
    }
}
