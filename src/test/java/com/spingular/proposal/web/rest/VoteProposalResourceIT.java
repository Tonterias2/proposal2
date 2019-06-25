package com.spingular.proposal.web.rest;

import com.spingular.proposal.SpingularproposalApp;
import com.spingular.proposal.domain.VoteProposal;
import com.spingular.proposal.domain.Proposal;
import com.spingular.proposal.domain.ProposalUser;
import com.spingular.proposal.repository.VoteProposalRepository;
import com.spingular.proposal.service.VoteProposalService;
import com.spingular.proposal.service.dto.VoteProposalDTO;
import com.spingular.proposal.service.mapper.VoteProposalMapper;
import com.spingular.proposal.web.rest.errors.ExceptionTranslator;
import com.spingular.proposal.service.dto.VoteProposalCriteria;
import com.spingular.proposal.service.VoteProposalQueryService;

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
 * Integration tests for the {@Link VoteProposalResource} REST controller.
 */
@SpringBootTest(classes = SpingularproposalApp.class)
public class VoteProposalResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_VOTE_POINTS = 1L;
    private static final Long UPDATED_VOTE_POINTS = 2L;

    @Autowired
    private VoteProposalRepository voteProposalRepository;

    @Autowired
    private VoteProposalMapper voteProposalMapper;

    @Autowired
    private VoteProposalService voteProposalService;

    @Autowired
    private VoteProposalQueryService voteProposalQueryService;

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

    private MockMvc restVoteProposalMockMvc;

    private VoteProposal voteProposal;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VoteProposalResource voteProposalResource = new VoteProposalResource(voteProposalService, voteProposalQueryService);
        this.restVoteProposalMockMvc = MockMvcBuilders.standaloneSetup(voteProposalResource)
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
    public static VoteProposal createEntity(EntityManager em) {
        VoteProposal voteProposal = new VoteProposal()
            .creationDate(DEFAULT_CREATION_DATE)
            .votePoints(DEFAULT_VOTE_POINTS);
        return voteProposal;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VoteProposal createUpdatedEntity(EntityManager em) {
        VoteProposal voteProposal = new VoteProposal()
            .creationDate(UPDATED_CREATION_DATE)
            .votePoints(UPDATED_VOTE_POINTS);
        return voteProposal;
    }

    @BeforeEach
    public void initTest() {
        voteProposal = createEntity(em);
    }

    @Test
    @Transactional
    public void createVoteProposal() throws Exception {
        int databaseSizeBeforeCreate = voteProposalRepository.findAll().size();

        // Create the VoteProposal
        VoteProposalDTO voteProposalDTO = voteProposalMapper.toDto(voteProposal);
        restVoteProposalMockMvc.perform(post("/api/vote-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voteProposalDTO)))
            .andExpect(status().isCreated());

        // Validate the VoteProposal in the database
        List<VoteProposal> voteProposalList = voteProposalRepository.findAll();
        assertThat(voteProposalList).hasSize(databaseSizeBeforeCreate + 1);
        VoteProposal testVoteProposal = voteProposalList.get(voteProposalList.size() - 1);
        assertThat(testVoteProposal.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testVoteProposal.getVotePoints()).isEqualTo(DEFAULT_VOTE_POINTS);
    }

    @Test
    @Transactional
    public void createVoteProposalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = voteProposalRepository.findAll().size();

        // Create the VoteProposal with an existing ID
        voteProposal.setId(1L);
        VoteProposalDTO voteProposalDTO = voteProposalMapper.toDto(voteProposal);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoteProposalMockMvc.perform(post("/api/vote-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voteProposalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VoteProposal in the database
        List<VoteProposal> voteProposalList = voteProposalRepository.findAll();
        assertThat(voteProposalList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = voteProposalRepository.findAll().size();
        // set the field null
        voteProposal.setCreationDate(null);

        // Create the VoteProposal, which fails.
        VoteProposalDTO voteProposalDTO = voteProposalMapper.toDto(voteProposal);

        restVoteProposalMockMvc.perform(post("/api/vote-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voteProposalDTO)))
            .andExpect(status().isBadRequest());

        List<VoteProposal> voteProposalList = voteProposalRepository.findAll();
        assertThat(voteProposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVotePointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = voteProposalRepository.findAll().size();
        // set the field null
        voteProposal.setVotePoints(null);

        // Create the VoteProposal, which fails.
        VoteProposalDTO voteProposalDTO = voteProposalMapper.toDto(voteProposal);

        restVoteProposalMockMvc.perform(post("/api/vote-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voteProposalDTO)))
            .andExpect(status().isBadRequest());

        List<VoteProposal> voteProposalList = voteProposalRepository.findAll();
        assertThat(voteProposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVoteProposals() throws Exception {
        // Initialize the database
        voteProposalRepository.saveAndFlush(voteProposal);

        // Get all the voteProposalList
        restVoteProposalMockMvc.perform(get("/api/vote-proposals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voteProposal.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].votePoints").value(hasItem(DEFAULT_VOTE_POINTS.intValue())));
    }
    
    @Test
    @Transactional
    public void getVoteProposal() throws Exception {
        // Initialize the database
        voteProposalRepository.saveAndFlush(voteProposal);

        // Get the voteProposal
        restVoteProposalMockMvc.perform(get("/api/vote-proposals/{id}", voteProposal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(voteProposal.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.votePoints").value(DEFAULT_VOTE_POINTS.intValue()));
    }

    @Test
    @Transactional
    public void getAllVoteProposalsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        voteProposalRepository.saveAndFlush(voteProposal);

        // Get all the voteProposalList where creationDate equals to DEFAULT_CREATION_DATE
        defaultVoteProposalShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the voteProposalList where creationDate equals to UPDATED_CREATION_DATE
        defaultVoteProposalShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllVoteProposalsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        voteProposalRepository.saveAndFlush(voteProposal);

        // Get all the voteProposalList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultVoteProposalShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the voteProposalList where creationDate equals to UPDATED_CREATION_DATE
        defaultVoteProposalShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllVoteProposalsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        voteProposalRepository.saveAndFlush(voteProposal);

        // Get all the voteProposalList where creationDate is not null
        defaultVoteProposalShouldBeFound("creationDate.specified=true");

        // Get all the voteProposalList where creationDate is null
        defaultVoteProposalShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllVoteProposalsByVotePointsIsEqualToSomething() throws Exception {
        // Initialize the database
        voteProposalRepository.saveAndFlush(voteProposal);

        // Get all the voteProposalList where votePoints equals to DEFAULT_VOTE_POINTS
        defaultVoteProposalShouldBeFound("votePoints.equals=" + DEFAULT_VOTE_POINTS);

        // Get all the voteProposalList where votePoints equals to UPDATED_VOTE_POINTS
        defaultVoteProposalShouldNotBeFound("votePoints.equals=" + UPDATED_VOTE_POINTS);
    }

    @Test
    @Transactional
    public void getAllVoteProposalsByVotePointsIsInShouldWork() throws Exception {
        // Initialize the database
        voteProposalRepository.saveAndFlush(voteProposal);

        // Get all the voteProposalList where votePoints in DEFAULT_VOTE_POINTS or UPDATED_VOTE_POINTS
        defaultVoteProposalShouldBeFound("votePoints.in=" + DEFAULT_VOTE_POINTS + "," + UPDATED_VOTE_POINTS);

        // Get all the voteProposalList where votePoints equals to UPDATED_VOTE_POINTS
        defaultVoteProposalShouldNotBeFound("votePoints.in=" + UPDATED_VOTE_POINTS);
    }

    @Test
    @Transactional
    public void getAllVoteProposalsByVotePointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        voteProposalRepository.saveAndFlush(voteProposal);

        // Get all the voteProposalList where votePoints is not null
        defaultVoteProposalShouldBeFound("votePoints.specified=true");

        // Get all the voteProposalList where votePoints is null
        defaultVoteProposalShouldNotBeFound("votePoints.specified=false");
    }

    @Test
    @Transactional
    public void getAllVoteProposalsByVotePointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        voteProposalRepository.saveAndFlush(voteProposal);

        // Get all the voteProposalList where votePoints greater than or equals to DEFAULT_VOTE_POINTS
        defaultVoteProposalShouldBeFound("votePoints.greaterOrEqualThan=" + DEFAULT_VOTE_POINTS);

        // Get all the voteProposalList where votePoints greater than or equals to UPDATED_VOTE_POINTS
        defaultVoteProposalShouldNotBeFound("votePoints.greaterOrEqualThan=" + UPDATED_VOTE_POINTS);
    }

    @Test
    @Transactional
    public void getAllVoteProposalsByVotePointsIsLessThanSomething() throws Exception {
        // Initialize the database
        voteProposalRepository.saveAndFlush(voteProposal);

        // Get all the voteProposalList where votePoints less than or equals to DEFAULT_VOTE_POINTS
        defaultVoteProposalShouldNotBeFound("votePoints.lessThan=" + DEFAULT_VOTE_POINTS);

        // Get all the voteProposalList where votePoints less than or equals to UPDATED_VOTE_POINTS
        defaultVoteProposalShouldBeFound("votePoints.lessThan=" + UPDATED_VOTE_POINTS);
    }


    @Test
    @Transactional
    public void getAllVoteProposalsByProposalIsEqualToSomething() throws Exception {
        // Initialize the database
        Proposal proposal = ProposalResourceIT.createEntity(em);
        em.persist(proposal);
        em.flush();
        voteProposal.setProposal(proposal);
        voteProposalRepository.saveAndFlush(voteProposal);
        Long proposalId = proposal.getId();

        // Get all the voteProposalList where proposal equals to proposalId
        defaultVoteProposalShouldBeFound("proposalId.equals=" + proposalId);

        // Get all the voteProposalList where proposal equals to proposalId + 1
        defaultVoteProposalShouldNotBeFound("proposalId.equals=" + (proposalId + 1));
    }


    @Test
    @Transactional
    public void getAllVoteProposalsByProposalUserIsEqualToSomething() throws Exception {
        // Initialize the database
        ProposalUser proposalUser = ProposalUserResourceIT.createEntity(em);
        em.persist(proposalUser);
        em.flush();
        voteProposal.setProposalUser(proposalUser);
        voteProposalRepository.saveAndFlush(voteProposal);
        Long proposalUserId = proposalUser.getId();

        // Get all the voteProposalList where proposalUser equals to proposalUserId
        defaultVoteProposalShouldBeFound("proposalUserId.equals=" + proposalUserId);

        // Get all the voteProposalList where proposalUser equals to proposalUserId + 1
        defaultVoteProposalShouldNotBeFound("proposalUserId.equals=" + (proposalUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVoteProposalShouldBeFound(String filter) throws Exception {
        restVoteProposalMockMvc.perform(get("/api/vote-proposals?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voteProposal.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].votePoints").value(hasItem(DEFAULT_VOTE_POINTS.intValue())));

        // Check, that the count call also returns 1
        restVoteProposalMockMvc.perform(get("/api/vote-proposals/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVoteProposalShouldNotBeFound(String filter) throws Exception {
        restVoteProposalMockMvc.perform(get("/api/vote-proposals?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVoteProposalMockMvc.perform(get("/api/vote-proposals/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingVoteProposal() throws Exception {
        // Get the voteProposal
        restVoteProposalMockMvc.perform(get("/api/vote-proposals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVoteProposal() throws Exception {
        // Initialize the database
        voteProposalRepository.saveAndFlush(voteProposal);

        int databaseSizeBeforeUpdate = voteProposalRepository.findAll().size();

        // Update the voteProposal
        VoteProposal updatedVoteProposal = voteProposalRepository.findById(voteProposal.getId()).get();
        // Disconnect from session so that the updates on updatedVoteProposal are not directly saved in db
        em.detach(updatedVoteProposal);
        updatedVoteProposal
            .creationDate(UPDATED_CREATION_DATE)
            .votePoints(UPDATED_VOTE_POINTS);
        VoteProposalDTO voteProposalDTO = voteProposalMapper.toDto(updatedVoteProposal);

        restVoteProposalMockMvc.perform(put("/api/vote-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voteProposalDTO)))
            .andExpect(status().isOk());

        // Validate the VoteProposal in the database
        List<VoteProposal> voteProposalList = voteProposalRepository.findAll();
        assertThat(voteProposalList).hasSize(databaseSizeBeforeUpdate);
        VoteProposal testVoteProposal = voteProposalList.get(voteProposalList.size() - 1);
        assertThat(testVoteProposal.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testVoteProposal.getVotePoints()).isEqualTo(UPDATED_VOTE_POINTS);
    }

    @Test
    @Transactional
    public void updateNonExistingVoteProposal() throws Exception {
        int databaseSizeBeforeUpdate = voteProposalRepository.findAll().size();

        // Create the VoteProposal
        VoteProposalDTO voteProposalDTO = voteProposalMapper.toDto(voteProposal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoteProposalMockMvc.perform(put("/api/vote-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voteProposalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VoteProposal in the database
        List<VoteProposal> voteProposalList = voteProposalRepository.findAll();
        assertThat(voteProposalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVoteProposal() throws Exception {
        // Initialize the database
        voteProposalRepository.saveAndFlush(voteProposal);

        int databaseSizeBeforeDelete = voteProposalRepository.findAll().size();

        // Delete the voteProposal
        restVoteProposalMockMvc.perform(delete("/api/vote-proposals/{id}", voteProposal.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VoteProposal> voteProposalList = voteProposalRepository.findAll();
        assertThat(voteProposalList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VoteProposal.class);
        VoteProposal voteProposal1 = new VoteProposal();
        voteProposal1.setId(1L);
        VoteProposal voteProposal2 = new VoteProposal();
        voteProposal2.setId(voteProposal1.getId());
        assertThat(voteProposal1).isEqualTo(voteProposal2);
        voteProposal2.setId(2L);
        assertThat(voteProposal1).isNotEqualTo(voteProposal2);
        voteProposal1.setId(null);
        assertThat(voteProposal1).isNotEqualTo(voteProposal2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VoteProposalDTO.class);
        VoteProposalDTO voteProposalDTO1 = new VoteProposalDTO();
        voteProposalDTO1.setId(1L);
        VoteProposalDTO voteProposalDTO2 = new VoteProposalDTO();
        assertThat(voteProposalDTO1).isNotEqualTo(voteProposalDTO2);
        voteProposalDTO2.setId(voteProposalDTO1.getId());
        assertThat(voteProposalDTO1).isEqualTo(voteProposalDTO2);
        voteProposalDTO2.setId(2L);
        assertThat(voteProposalDTO1).isNotEqualTo(voteProposalDTO2);
        voteProposalDTO1.setId(null);
        assertThat(voteProposalDTO1).isNotEqualTo(voteProposalDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(voteProposalMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(voteProposalMapper.fromId(null)).isNull();
    }
}
