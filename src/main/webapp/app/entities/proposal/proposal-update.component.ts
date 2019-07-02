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
import { IProfile } from 'app/shared/model/profile.model';
import { ProfileService } from 'app/entities/profile';
import { IPost } from 'app/shared/model/post.model';
import { PostService } from 'app/entities/post';

@Component({
  selector: 'jhi-proposal-update',
  templateUrl: './proposal-update.component.html'
})
export class ProposalUpdateComponent implements OnInit {
  isSaving: boolean;

  profiles: IProfile[];
  profile: IProfile;

  posts: IPost[];

  account: any;
  nameParamFollows: any;
  valueParamFollows: any;
  userQuery: boolean;

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    proposalName: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(250)]],
    proposalType: [null, [Validators.required]],
    proposalRole: [null, [Validators.required]],
    releaseDate: [],
    isOpen: [],
    isAccepted: [],
    isPaid: [],
    profileId: [],
    postId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected proposalService: ProposalService,
    protected profileService: ProfileService,
    protected postService: PostService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {
    this.activatedRoute.queryParams.subscribe(params => {
      if (params.postIdEquals != null) {
        this.nameParamFollows = 'postId.equals';
        this.valueParamFollows = params.postIdEquals;
        this.userQuery = true;
      }
    });
  }

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ proposal }) => {
      this.updateForm(proposal);
    });
    this.profileService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IProfile[]>) => mayBeOk.ok),
        map((response: HttpResponse<IProfile[]>) => response.body)
      )
      .subscribe((res: IProfile[]) => (this.profiles = res), (res: HttpErrorResponse) => this.onError(res.message));
    //    this.postService
    //      .query()
    //      .pipe(
    //        filter((mayBeOk: HttpResponse<IPost[]>) => mayBeOk.ok),
    //        map((response: HttpResponse<IPost[]>) => response.body)
    //      )
    //      .subscribe((res: IPost[]) => (this.posts = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(proposal: IProposal) {
    this.editForm.patchValue({
      id: proposal.id,
      creationDate: proposal.creationDate != null ? proposal.creationDate.format(DATE_TIME_FORMAT) : null,
      proposalName: proposal.proposalName,
      proposalType: proposal.proposalType,
      proposalRole: proposal.proposalRole,
      releaseDate: proposal.releaseDate != null ? proposal.releaseDate.format(DATE_TIME_FORMAT) : null,
      isOpen: proposal.isOpen,
      isAccepted: proposal.isAccepted,
      isPaid: proposal.isPaid,
      profileId: proposal.profileId,
      postId: proposal.postId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const proposal = this.createFromForm();
    //    const study = 'STUDY';
    if (proposal.id !== undefined) {
      this.subscribeToSaveResponse(this.proposalService.update(proposal));
    } else {
      //      proposal.proposalType = study;
      //      proposal.proposalRole = proposal.proposalRole.USER;
      //        proposal.proposalRole = ProposalRole.USER;
      //        proposal.proposalRole = proposalRole.USER;
      proposal.profileId = this.profile.id;
      proposal.postId = this.valueParamFollows;
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
      isOpen: this.editForm.get(['isOpen']).value,
      isAccepted: this.editForm.get(['isAccepted']).value,
      isPaid: this.editForm.get(['isPaid']).value,
      profileId: this.editForm.get(['profileId']).value,
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

  trackProfileById(index: number, item: IProfile) {
    return item.id;
  }

  trackPostById(index: number, item: IPost) {
    return item.id;
  }
}
