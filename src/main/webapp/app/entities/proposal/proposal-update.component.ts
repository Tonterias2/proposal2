import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IProposal, Proposal } from 'app/shared/model/proposal.model';
import { ProposalService } from './proposal.service';
import { IProposalUser } from 'app/shared/model/proposal-user.model';
import { ProposalUserService } from 'app/entities/proposal-user';
import { IPost } from 'app/shared/model/post.model';
import { PostService } from 'app/entities/post';

@Component({
  selector: 'jhi-proposal-update',
  templateUrl: './proposal-update.component.html'
})
export class ProposalUpdateComponent implements OnInit {
  isSaving: boolean;

  proposalusers: IProposalUser[];

  posts: IPost[];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    proposalName: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(250)]],
    proposalType: [null, [Validators.required]],
    proposalRole: [null, [Validators.required]],
    releaseDate: [],
    proposalVotes: [],
    proposalUserVotes: [],
    proposalUserId: [],
    postId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected proposalService: ProposalService,
    protected proposalUserService: ProposalUserService,
    protected postService: PostService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ proposal }) => {
      this.updateForm(proposal);
    });
    this.proposalUserService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IProposalUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IProposalUser[]>) => response.body)
      )
      .subscribe((res: IProposalUser[]) => (this.proposalusers = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.postService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPost[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPost[]>) => response.body)
      )
      .subscribe((res: IPost[]) => (this.posts = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(proposal: IProposal) {
    this.editForm.patchValue({
      id: proposal.id,
      creationDate: proposal.creationDate != null ? proposal.creationDate.format(DATE_TIME_FORMAT) : null,
      proposalName: proposal.proposalName,
      proposalType: proposal.proposalType,
      proposalRole: proposal.proposalRole,
      releaseDate: proposal.releaseDate != null ? proposal.releaseDate.format(DATE_TIME_FORMAT) : null,
      proposalVotes: proposal.proposalVotes,
      proposalUserVotes: proposal.proposalUserVotes,
      proposalUserId: proposal.proposalUserId,
      postId: proposal.postId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const proposal = this.createFromForm();
    if (proposal.id !== undefined) {
      this.subscribeToSaveResponse(this.proposalService.update(proposal));
    } else {
      this.subscribeToSaveResponse(this.proposalService.create(proposal));
    }
  }

  private createFromForm(): IProposal {
    return {
      ...new Proposal(),
      id: this.editForm.get(['id']).value,
      creationDate:
        this.editForm.get(['creationDate']).value != null ? moment(this.editForm.get(['creationDate']).value, DATE_TIME_FORMAT) : undefined,
      proposalName: this.editForm.get(['proposalName']).value,
      proposalType: this.editForm.get(['proposalType']).value,
      proposalRole: this.editForm.get(['proposalRole']).value,
      releaseDate:
        this.editForm.get(['releaseDate']).value != null ? moment(this.editForm.get(['releaseDate']).value, DATE_TIME_FORMAT) : undefined,
      proposalVotes: this.editForm.get(['proposalVotes']).value,
      proposalUserVotes: this.editForm.get(['proposalUserVotes']).value,
      proposalUserId: this.editForm.get(['proposalUserId']).value,
      postId: this.editForm.get(['postId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProposal>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackProposalUserById(index: number, item: IProposalUser) {
    return item.id;
  }

  trackPostById(index: number, item: IPost) {
    return item.id;
  }
}