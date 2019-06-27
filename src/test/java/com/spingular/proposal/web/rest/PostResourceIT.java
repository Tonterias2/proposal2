package com.spingular.proposal.web.rest;

import com.spingular.proposal.SpingularproposalApp;
import com.spingular.proposal.domain.Post;
import com.spingular.proposal.domain.Proposal;
import com.spingular.proposal.repository.PostRepository;
import com.spingular.proposal.service.PostService;
import com.spingular.proposal.service.dto.PostDTO;
import com.spingular.proposal.service.mapper.PostMapper;
import com.spingular.proposal.web.rest.errors.ExceptionTranslator;
import com.spingular.proposal.service.dto.PostCriteria;
import com.spingular.proposal.service.PostQueryService;

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
 * Integration tests for the {@Link PostResource} REST controller.
 */
@SpringBootTest(classes = SpingularproposalApp.class)
public class PostResourceIT {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_POST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_POST_NAME = "BBBBBBBBBB";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostService postService;

    @Autowired
    private PostQueryService postQueryService;

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

    private MockMvc restPostMockMvc;

    private Post post;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PostResource postResource = new PostResource(postService, postQueryService);
        this.restPostMockMvc = MockMvcBuilders.standaloneSetup(postResource)
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
    public static Post createEntity(EntityManager em) {
        Post post = new Post()
            .creationDate(DEFAULT_CREATION_DATE)
            .postName(DEFAULT_POST_NAME);
        return post;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Post createUpdatedEntity(EntityManager em) {
        Post post = new Post()
            .creationDate(UPDATED_CREATION_DATE)
            .postName(UPDATED_POST_NAME);
        return post;
    }

    @BeforeEach
    public void initTest() {
        post = createEntity(em);
    }

    @Test
    @Transactional
    public void createPost() throws Exception {
        int databaseSizeBeforeCreate = postRepository.findAll().size();

        // Create the Post
        PostDTO postDTO = postMapper.toDto(post);
        restPostMockMvc.perform(post("/api/posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postDTO)))
            .andExpect(status().isCreated());

        // Validate the Post in the database
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeCreate + 1);
        Post testPost = postList.get(postList.size() - 1);
        assertThat(testPost.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testPost.getPostName()).isEqualTo(DEFAULT_POST_NAME);
    }

    @Test
    @Transactional
    public void createPostWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = postRepository.findAll().size();

        // Create the Post with an existing ID
        post.setId(1L);
        PostDTO postDTO = postMapper.toDto(post);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostMockMvc.perform(post("/api/posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Post in the database
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = postRepository.findAll().size();
        // set the field null
        post.setCreationDate(null);

        // Create the Post, which fails.
        PostDTO postDTO = postMapper.toDto(post);

        restPostMockMvc.perform(post("/api/posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postDTO)))
            .andExpect(status().isBadRequest());

        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPostNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = postRepository.findAll().size();
        // set the field null
        post.setPostName(null);

        // Create the Post, which fails.
        PostDTO postDTO = postMapper.toDto(post);

        restPostMockMvc.perform(post("/api/posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postDTO)))
            .andExpect(status().isBadRequest());

        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPosts() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList
        restPostMockMvc.perform(get("/api/posts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(post.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].postName").value(hasItem(DEFAULT_POST_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getPost() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get the post
        restPostMockMvc.perform(get("/api/posts/{id}", post.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(post.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.postName").value(DEFAULT_POST_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllPostsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where creationDate equals to DEFAULT_CREATION_DATE
        defaultPostShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the postList where creationDate equals to UPDATED_CREATION_DATE
        defaultPostShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllPostsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultPostShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the postList where creationDate equals to UPDATED_CREATION_DATE
        defaultPostShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllPostsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where creationDate is not null
        defaultPostShouldBeFound("creationDate.specified=true");

        // Get all the postList where creationDate is null
        defaultPostShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostsByPostNameIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where postName equals to DEFAULT_POST_NAME
        defaultPostShouldBeFound("postName.equals=" + DEFAULT_POST_NAME);

        // Get all the postList where postName equals to UPDATED_POST_NAME
        defaultPostShouldNotBeFound("postName.equals=" + UPDATED_POST_NAME);
    }

    @Test
    @Transactional
    public void getAllPostsByPostNameIsInShouldWork() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where postName in DEFAULT_POST_NAME or UPDATED_POST_NAME
        defaultPostShouldBeFound("postName.in=" + DEFAULT_POST_NAME + "," + UPDATED_POST_NAME);

        // Get all the postList where postName equals to UPDATED_POST_NAME
        defaultPostShouldNotBeFound("postName.in=" + UPDATED_POST_NAME);
    }

    @Test
    @Transactional
    public void getAllPostsByPostNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where postName is not null
        defaultPostShouldBeFound("postName.specified=true");

        // Get all the postList where postName is null
        defaultPostShouldNotBeFound("postName.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostsByProposalIsEqualToSomething() throws Exception {
        // Initialize the database
        Proposal proposal = ProposalResourceIT.createEntity(em);
        em.persist(proposal);
        em.flush();
        post.addProposal(proposal);
        postRepository.saveAndFlush(post);
        Long proposalId = proposal.getId();

        // Get all the postList where proposal equals to proposalId
        defaultPostShouldBeFound("proposalId.equals=" + proposalId);

        // Get all the postList where proposal equals to proposalId + 1
        defaultPostShouldNotBeFound("proposalId.equals=" + (proposalId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPostShouldBeFound(String filter) throws Exception {
        restPostMockMvc.perform(get("/api/posts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(post.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].postName").value(hasItem(DEFAULT_POST_NAME)));

        // Check, that the count call also returns 1
        restPostMockMvc.perform(get("/api/posts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPostShouldNotBeFound(String filter) throws Exception {
        restPostMockMvc.perform(get("/api/posts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPostMockMvc.perform(get("/api/posts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPost() throws Exception {
        // Get the post
        restPostMockMvc.perform(get("/api/posts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePost() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        int databaseSizeBeforeUpdate = postRepository.findAll().size();

        // Update the post
        Post updatedPost = postRepository.findById(post.getId()).get();
        // Disconnect from session so that the updates on updatedPost are not directly saved in db
        em.detach(updatedPost);
        updatedPost
            .creationDate(UPDATED_CREATION_DATE)
            .postName(UPDATED_POST_NAME);
        PostDTO postDTO = postMapper.toDto(updatedPost);

        restPostMockMvc.perform(put("/api/posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postDTO)))
            .andExpect(status().isOk());

        // Validate the Post in the database
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeUpdate);
        Post testPost = postList.get(postList.size() - 1);
        assertThat(testPost.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testPost.getPostName()).isEqualTo(UPDATED_POST_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingPost() throws Exception {
        int databaseSizeBeforeUpdate = postRepository.findAll().size();

        // Create the Post
        PostDTO postDTO = postMapper.toDto(post);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostMockMvc.perform(put("/api/posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Post in the database
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePost() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        int databaseSizeBeforeDelete = postRepository.findAll().size();

        // Delete the post
        restPostMockMvc.perform(delete("/api/posts/{id}", post.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Post.class);
        Post post1 = new Post();
        post1.setId(1L);
        Post post2 = new Post();
        post2.setId(post1.getId());
        assertThat(post1).isEqualTo(post2);
        post2.setId(2L);
        assertThat(post1).isNotEqualTo(post2);
        post1.setId(null);
        assertThat(post1).isNotEqualTo(post2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostDTO.class);
        PostDTO postDTO1 = new PostDTO();
        postDTO1.setId(1L);
        PostDTO postDTO2 = new PostDTO();
        assertThat(postDTO1).isNotEqualTo(postDTO2);
        postDTO2.setId(postDTO1.getId());
        assertThat(postDTO1).isEqualTo(postDTO2);
        postDTO2.setId(2L);
        assertThat(postDTO1).isNotEqualTo(postDTO2);
        postDTO1.setId(null);
        assertThat(postDTO1).isNotEqualTo(postDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(postMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(postMapper.fromId(null)).isNull();
    }
}
