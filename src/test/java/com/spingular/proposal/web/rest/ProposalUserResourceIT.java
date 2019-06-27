package com.spingular.proposal.web.rest;

import com.spingular.proposal.SpingularproposalApp;
import com.spingular.proposal.domain.ProposalUser;
import com.spingular.proposal.domain.User;
import com.spingular.proposal.domain.Proposal;
import com.spingular.proposal.domain.VoteProposal;
import com.spingular.proposal.repository.ProposalUserRepository;
import com.spingular.proposal.service.ProposalUserService;
import com.spingular.proposal.service.dto.ProposalUserDTO;
import com.spingular.proposal.service.mapper.ProposalUserMapper;
import com.spingular.proposal.web.rest.errors.ExceptionTranslator;
import com.spingular.proposal.service.dto.ProposalUserCriteria;
import com.spingular.proposal.service.ProposalUserQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.spingular.proposal.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link ProposalUserResource} REST controller.
 */
@SpringBootTest(classes = SpingularproposalApp.class)
public class ProposalUserResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_ASSIGNED_VOTES_POINTS = 1L;
    private static final Long UPDATED_ASSIGNED_VOTES_POINTS = 2L;

    @Autowired
    private ProposalUserRepository proposalUserRepository;

    @Autowired
    private ProposalUserMapper proposalUserMapper;

    @Autowired
    private ProposalUserService proposalUserService;

    @Autowired
    private ProposalUserQueryService proposalUserQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restProposalUserMockMvc;

    private ProposalUser proposalUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProposalUserResource proposalUserResource = new ProposalUserResource(proposalUserService, proposalUserQueryService);
        this.restProposalUserMockMvc = MockMvcBuilders.standaloneSetup(proposalUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProposalUser createEntity(EntityManager em) {
        ProposalUser proposalUser = new ProposalUser()
            .creationDate(DEFAULT_CREATION_DATE)
            .assignedVotesPoints(DEFAULT_ASSIGNED_VOTES_POINTS);
        return proposalUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProposalUser createUpdatedEntity(EntityManager em) {
        ProposalUser proposalUser = new ProposalUser()
            .creationDate(UPDATED_CREATION_DATE)
            .assignedVotesPoints(UPDATED_ASSIGNED_VOTES_POINTS);
        return proposalUser;
    }

    @BeforeEach
    public void initTest() {
        proposalUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createProposalUser() throws Exception {
        int databaseSizeBeforeCreate = proposalUserRepository.findAll().size();

        // Create the ProposalUser
        ProposalUserDTO proposalUserDTO = proposalUserMapper.toDto(proposalUser);
        restProposalUserMockMvc.perform(post("/api/proposal-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proposalUserDTO)))
            .andExpect(status().isCreated());

        // Validate the ProposalUser in the database
        List<ProposalUser> proposalUserList = proposalUserRepository.findAll();
        assertThat(proposalUserList).hasSize(databaseSizeBeforeCreate + 1);
        ProposalUser testProposalUser = proposalUserList.get(proposalUserList.size() - 1);
        assertThat(testProposalUser.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testProposalUser.getAssignedVotesPoints()).isEqualTo(DEFAULT_ASSIGNED_VOTES_POINTS);
    }

    @Test
    @Transactional
    public void createProposalUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = proposalUserRepository.findAll().size();

        // Create the ProposalUser with an existing ID
        proposalUser.setId(1L);
        ProposalUserDTO proposalUserDTO = proposalUserMapper.toDto(proposalUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProposalUserMockMvc.perform(post("/api/proposal-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proposalUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProposalUser in the database
        List<ProposalUser> proposalUserList = proposalUserRepository.findAll();
        assertThat(proposalUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = proposalUserRepository.findAll().size();
        // set the field null
        proposalUser.setCreationDate(null);

        // Create the ProposalUser, which fails.
        ProposalUserDTO proposalUserDTO = proposalUserMapper.toDto(proposalUser);

        restProposalUserMockMvc.perform(post("/api/proposal-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proposalUserDTO)))
            .andExpect(status().isBadRequest());

        List<ProposalUser> proposalUserList = proposalUserRepository.findAll();
        assertThat(proposalUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProposalUsers() throws Exception {
        // Initialize the database
        proposalUserRepository.saveAndFlush(proposalUser);

        // Get all the proposalUserList
        restProposalUserMockMvc.perform(get("/api/proposal-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposalUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].assignedVotesPoints").value(hasItem(DEFAULT_ASSIGNED_VOTES_POINTS.intValue())));
    }
    
    @Test
    @Transactional
    public void getProposalUser() throws Exception {
        // Initialize the database
        proposalUserRepository.saveAndFlush(proposalUser);

        // Get the proposalUser
        restProposalUserMockMvc.perform(get("/api/proposal-users/{id}", proposalUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(proposalUser.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.assignedVotesPoints").value(DEFAULT_ASSIGNED_VOTES_POINTS.intValue()));
    }

    @Test
    @Transactional
    public void getAllProposalUsersByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        proposalUserRepository.saveAndFlush(proposalUser);

        // Get all the proposalUserList where creationDate equals to DEFAULT_CREATION_DATE
        defaultProposalUserShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the proposalUserList where creationDate equals to UPDATED_CREATION_DATE
        defaultProposalUserShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProposalUsersByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        proposalUserRepository.saveAndFlush(proposalUser);

        // Get all the proposalUserList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultProposalUserShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the proposalUserList where creationDate equals to UPDATED_CREATION_DATE
        defaultProposalUserShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProposalUsersByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        proposalUserRepository.saveAndFlush(proposalUser);

        // Get all the proposalUserList where creationDate is not null
        defaultProposalUserShouldBeFound("creationDate.specified=true");

        // Get all the proposalUserList where creationDate is null
        defaultProposalUserShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllProposalUsersByAssignedVotesPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        proposalUserRepository.saveAndFlush(proposalUser);

        // Get all the proposalUserList where assignedVotesPoints equals to DEFAULT_ASSIGNED_VOTES_POINTS
        defaultProposalUserShouldBeFound("assignedVotesPoints.equals=" + DEFAULT_ASSIGNED_VOTES_POINTS);

        // Get all the proposalUserList where assignedVotesPoints equals to UPDATED_ASSIGNED_VOTES_POINTS
        defaultProposalUserShouldNotBeFound("assignedVotesPoints.equals=" + UPDATED_ASSIGNED_VOTES_POINTS);
    }

    @Test
    @Transactional
    public void getAllProposalUsersByAssignedVotesPointsIsInShouldWork() throws Exception {
        // Initialize the database
        proposalUserRepository.saveAndFlush(proposalUser);

        // Get all the proposalUserList where assignedVotesPoints in DEFAULT_ASSIGNED_VOTES_POINTS or UPDATED_ASSIGNED_VOTES_POINTS
        defaultProposalUserShouldBeFound("assignedVotesPoints.in=" + DEFAULT_ASSIGNED_VOTES_POINTS + "," + UPDATED_ASSIGNED_VOTES_POINTS);

        // Get all the proposalUserList where assignedVotesPoints equals to UPDATED_ASSIGNED_VOTES_POINTS
        defaultProposalUserShouldNotBeFound("assignedVotesPoints.in=" + UPDATED_ASSIGNED_VOTES_POINTS);
    }

    @Test
    @Transactional
    public void getAllProposalUsersByAssignedVotesPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        proposalUserRepository.saveAndFlush(proposalUser);

        // Get all the proposalUserList where assignedVotesPoints is not null
        defaultProposalUserShouldBeFound("assignedVotesPoints.specified=true");

        // Get all the proposalUserList where assignedVotesPoints is null
        defaultProposalUserShouldNotBeFound("assignedVotesPoints.specified=false");
    }

    @Test
    @Transactional
    public void getAllProposalUsersByAssignedVotesPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        proposalUserRepository.saveAndFlush(proposalUser);

        // Get all the proposalUserList where assignedVotesPoints greater than or equals to DEFAULT_ASSIGNED_VOTES_POINTS
        defaultProposalUserShouldBeFound("assignedVotesPoints.greaterOrEqualThan=" + DEFAULT_ASSIGNED_VOTES_POINTS);

        // Get all the proposalUserList where assignedVotesPoints greater than or equals to UPDATED_ASSIGNED_VOTES_POINTS
        defaultProposalUserShouldNotBeFound("assignedVotesPoints.greaterOrEqualThan=" + UPDATED_ASSIGNED_VOTES_POINTS);
    }

    @Test
    @Transactional
    public void getAllProposalUsersByAssignedVotesPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        proposalUserRepository.saveAndFlush(proposalUser);

        // Get all the proposalUserList where assignedVotesPoints less than or equals to DEFAULT_ASSIGNED_VOTES_POINTS
        defaultProposalUserShouldNotBeFound("assignedVotesPoints.lessThan=" + DEFAULT_ASSIGNED_VOTES_POINTS);

        // Get all the proposalUserList where assignedVotesPoints less than or equals to UPDATED_ASSIGNED_VOTES_POINTS
        defaultProposalUserShouldBeFound("assignedVotesPoints.lessThan=" + UPDATED_ASSIGNED_VOTES_POINTS);
    }


    @Test
    @Transactional
    public void getAllProposalUsersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        proposalUser.setUser(user);
        proposalUserRepository.saveAndFlush(proposalUser);
        Long userId = user.getId();

        // Get all the proposalUserList where user equals to userId
        defaultProposalUserShouldBeFound("userId.equals=" + userId);

        // Get all the proposalUserList where user equals to userId + 1
        defaultProposalUserShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllProposalUsersByProposalIsEqualToSomething() throws Exception {
        // Initialize the database
        Proposal proposal = ProposalResourceIT.createEntity(em);
        em.persist(proposal);
        em.flush();
        proposalUser.addProposal(proposal);
        proposalUserRepository.saveAndFlush(proposalUser);
        Long proposalId = proposal.getId();

        // Get all the proposalUserList where proposal equals to proposalId
        defaultProposalUserShouldBeFound("proposalId.equals=" + proposalId);

        // Get all the proposalUserList where proposal equals to proposalId + 1
        defaultProposalUserShouldNotBeFound("proposalId.equals=" + (proposalId + 1));
    }


    @Test
    @Transactional
    public void getAllProposalUsersByVoteProposalIsEqualToSomething() throws Exception {
        // Initialize the database
        VoteProposal voteProposal = VoteProposalResourceIT.createEntity(em);
        em.persist(voteProposal);
        em.flush();
        proposalUser.addVoteProposal(voteProposal);
        proposalUserRepository.saveAndFlush(proposalUser);
        Long voteProposalId = voteProposal.getId();

        // Get all the proposalUserList where voteProposal equals to voteProposalId
        defaultProposalUserShouldBeFound("voteProposalId.equals=" + voteProposalId);

        // Get all the proposalUserList where voteProposal equals to voteProposalId + 1
        defaultProposalUserShouldNotBeFound("voteProposalId.equals=" + (voteProposalId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProposalUserShouldBeFound(String filter) throws Exception {
        restProposalUserMockMvc.perform(get("/api/proposal-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposalUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].assignedVotesPoints").value(hasItem(DEFAULT_ASSIGNED_VOTES_POINTS.intValue())));

        // Check, that the count call also returns 1
        restProposalUserMockMvc.perform(get("/api/proposal-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProposalUserShouldNotBeFound(String filter) throws Exception {
        restProposalUserMockMvc.perform(get("/api/proposal-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProposalUserMockMvc.perform(get("/api/proposal-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProposalUser() throws Exception {
        // Get the proposalUser
        restProposalUserMockMvc.perform(get("/api/proposal-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProposalUser() throws Exception {
        // Initialize the database
        proposalUserRepository.saveAndFlush(proposalUser);

        int databaseSizeBeforeUpdate = proposalUserRepository.findAll().size();

        // Update the proposalUser
        ProposalUser updatedProposalUser = proposalUserRepository.findById(proposalUser.getId()).get();
        // Disconnect from session so that the updates on updatedProposalUser are not directly saved in db
        em.detach(updatedProposalUser);
        updatedProposalUser
            .creationDate(UPDATED_CREATION_DATE)
            .assignedVotesPoints(UPDATED_ASSIGNED_VOTES_POINTS);
        ProposalUserDTO proposalUserDTO = proposalUserMapper.toDto(updatedProposalUser);

        restProposalUserMockMvc.perform(put("/api/proposal-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proposalUserDTO)))
            .andExpect(status().isOk());

        // Validate the ProposalUser in the database
        List<ProposalUser> proposalUserList = proposalUserRepository.findAll();
        assertThat(proposalUserList).hasSize(databaseSizeBeforeUpdate);
        ProposalUser testProposalUser = proposalUserList.get(proposalUserList.size() - 1);
        assertThat(testProposalUser.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testProposalUser.getAssignedVotesPoints()).isEqualTo(UPDATED_ASSIGNED_VOTES_POINTS);
    }

    @Test
    @Transactional
    public void updateNonExistingProposalUser() throws Exception {
        int databaseSizeBeforeUpdate = proposalUserRepository.findAll().size();

        // Create the ProposalUser
        ProposalUserDTO proposalUserDTO = proposalUserMapper.toDto(proposalUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProposalUserMockMvc.perform(put("/api/proposal-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(proposalUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProposalUser in the database
        List<ProposalUser> proposalUserList = proposalUserRepository.findAll();
        assertThat(proposalUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProposalUser() throws Exception {
        // Initialize the database
        proposalUserRepository.saveAndFlush(proposalUser);

        int databaseSizeBeforeDelete = proposalUserRepository.findAll().size();

        // Delete the proposalUser
        restProposalUserMockMvc.perform(delete("/api/proposal-users/{id}", proposalUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProposalUser> proposalUserList = proposalUserRepository.findAll();
        assertThat(proposalUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProposalUser.class);
        ProposalUser proposalUser1 = new ProposalUser();
        proposalUser1.setId(1L);
        ProposalUser proposalUser2 = new ProposalUser();
        proposalUser2.setId(proposalUser1.getId());
        assertThat(proposalUser1).isEqualTo(proposalUser2);
        proposalUser2.setId(2L);
        assertThat(proposalUser1).isNotEqualTo(proposalUser2);
        proposalUser1.setId(null);
        assertThat(proposalUser1).isNotEqualTo(proposalUser2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProposalUserDTO.class);
        ProposalUserDTO proposalUserDTO1 = new ProposalUserDTO();
        proposalUserDTO1.setId(1L);
        ProposalUserDTO proposalUserDTO2 = new ProposalUserDTO();
        assertThat(proposalUserDTO1).isNotEqualTo(proposalUserDTO2);
        proposalUserDTO2.setId(proposalUserDTO1.getId());
        assertThat(proposalUserDTO1).isEqualTo(proposalUserDTO2);
        proposalUserDTO2.setId(2L);
        assertThat(proposalUserDTO1).isNotEqualTo(proposalUserDTO2);
        proposalUserDTO1.setId(null);
        assertThat(proposalUserDTO1).isNotEqualTo(proposalUserDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(proposalUserMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(proposalUserMapper.fromId(null)).isNull();
    }
}
