import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IVoteProposal, VoteProposal } from 'app/shared/model/vote-proposal.model';
import { VoteProposalService } from './vote-proposal.service';
import { IProposal } from 'app/shared/model/proposal.model';
import { ProposalService } from 'app/entities/proposal';
import { IProfile } from 'app/shared/model/profile.model';
import { ProfileService } from 'app/entities/profile';
import { ITEMS_PER_PAGE } from 'app/shared';

import { AccountService } from 'app/core';

@Component({
  selector: 'jhi-vote-proposal-update',
  templateUrl: './vote-proposal-update.component.html'
})
export class VoteProposalUpdateComponent implements OnInit {
  isSaving: boolean;

  proposals: IProposal[];
  proposal: IProposal;

  profiles: IProfile[];
  profile: IProfile;

  voteProposals: IVoteProposal[];

  error: any;
  success: any;
  routeData: any;
  links: any;
  totalItems: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  owner: any;
  isAdmin: boolean;

  currentAccount: any;

  nameParamFollows: any;
  valueParamFollows: any;
  userQuery: boolean;

  totalProposalVotes: number;
  userProposalVotes: number;
  userAvailableProposalVotes: number;

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    votePoints: [null, [Validators.required]],
    proposalId: [],
    profileId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected voteProposalService: VoteProposalService,
    protected proposalService: ProposalService,
    protected profileService: ProfileService,
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService,
    private fb: FormBuilder
  ) {
    this.activatedRoute.queryParams.subscribe(params => {
      if (params.proposalIdEquals != null) {
        this.nameParamFollows = 'proposalId.equals';
        this.valueParamFollows = params.proposalIdEquals;
        this.userQuery = true;
      }
    });
  }

  ngOnInit() {
    this.isSaving = false;
    this.accountService.identity().then(account => {
      this.currentAccount = account;
      //        console.log('CONSOLOG: M:ngOnInit & O: account : ', account);
      //        console.log('CONSOLOG: M:ngOnInit & O: this.currentAccount : ', this.currentAccount);
      //        this.owner = account.id;
      this.isAdmin = this.accountService.hasAnyAuthority(['ROLE_ADMIN']);
      const query = {};
      query['id.equals'] = this.currentAccount.id;
      //        console.log('CONSOLOG: M:ngOnInit & O: query : ', query);
      this.profileService.query(query).subscribe(
        (res: HttpResponse<IProfile[]>) => {
          this.profile = res.body[0];
          console.log('CONSOLOG: M:ngOnInit & O: this.profile : ', this.profile);
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
      const query4 = {};
      query['id.equals'] = this.valueParamFollows;
      console.log('CONSOLOG: M:ngOnInit & O: query4 : ', query4);
      this.proposalService.query(query4).subscribe(
        (res4: HttpResponse<IProposal[]>) => {
          this.proposal = res4.body[0];
          console.log('CONSOLOG: M:ngOnInit & O: this.proposal : ', this.proposal);
        },
        (res4: HttpErrorResponse) => this.onError(res4.message)
      );
      const query2 = {};
      query2['proposalId.equals'] = this.valueParamFollows;
      //      console.log('CONSOLOG: M:ngOnInit & O: query2 : ', query2);
      this.voteProposalService.query(query2).subscribe(
        (res2: HttpResponse<IVoteProposal[]>) => {
          this.voteProposals = [];
          this.voteProposals = res2.body;
          //          console.log('CONSOLOG: M:ngOnInit & O: this.voteProposals : ', this.voteProposals);
          this.totalProposalVotes = 0;
          this.userProposalVotes = 0;
          this.voteProposals.forEach(voteProposal => {
            this.totalProposalVotes = this.totalProposalVotes + voteProposal.votePoints;
            //              console.log('CONSOLOG: M:ngOnInit & O: this.totalProposalVotes : ', this.totalProposalVotes);
            if (voteProposal.profileId === this.profile.userId) {
              this.userProposalVotes = this.userProposalVotes + voteProposal.votePoints;
              //                  console.log('CONSOLOG: M:ngOnInit & O: this.userProposalVotes : ', this.userProposalVotes);
            }
          });
        },
        (res2: HttpErrorResponse) => this.onError(res2.message)
      );
      const query3 = {};
      query3['proposalUserId.equals'] = this.currentAccount.id;
      //      console.log('CONSOLOG: M:ngOnInit & O: query3 : ', query3);
      this.voteProposalService.query(query3).subscribe(
        (res3: HttpResponse<IVoteProposal[]>) => {
          this.voteProposals = [];
          this.voteProposals = res3.body;
          //          console.log('CONSOLOG: M:ngOnInit & O: this.voteProposals : ', this.voteProposals);
          this.userAvailableProposalVotes = this.profile.assignedVotesPoints;
          this.voteProposals.forEach(voteProposal => {
            //              console.log('CONSOLOG: M:ngOnInit & O: this.proposaluser.assignedVotesPoints : ', this.proposaluser.assignedVotesPoints);
            this.userAvailableProposalVotes = this.userAvailableProposalVotes - voteProposal.votePoints;
            //              console.log('CONSOLOG: M:ngOnInit & O: this.userAvailableProposalVotes : ', this.userAvailableProposalVotes);
          });
        },
        (res3: HttpErrorResponse) => this.onError(res3.message)
      );
    });
    this.activatedRoute.data.subscribe(({ voteProposal }) => {
      this.updateForm(voteProposal);
    });
    this.proposalService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IProposal[]>) => mayBeOk.ok),
        map((response: HttpResponse<IProposal[]>) => response.body)
      )
      .subscribe((res: IProposal[]) => (this.proposals = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.profileService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IProfile[]>) => mayBeOk.ok),
        map((response: HttpResponse<IProfile[]>) => response.body)
      )
      .subscribe((res: IProfile[]) => (this.profiles = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(voteProposal: IVoteProposal) {
    const date = moment(moment().format('YYYY-MM-DDTHH:mm'), 'YYYY-MM-DDTHH:mm');
    this.editForm.patchValue({
      id: voteProposal.id,
      //      creationDate: voteProposal.creationDate != null ? voteProposal.creationDate.format(DATE_TIME_FORMAT) : null,
      creationDate:
        voteProposal.creationDate != null
          ? voteProposal.creationDate.format(DATE_TIME_FORMAT)
          : JSON.stringify(date)
              .split(':00.000Z')
              .join('')
              .split('"')
              .join(''),
      votePoints: voteProposal.votePoints,
      proposalId: voteProposal.proposalId,
      profileId: voteProposal.profileId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const voteProposal = this.createFromForm();
    if (voteProposal.id !== undefined) {
      this.subscribeToSaveResponse(this.voteProposalService.update(voteProposal));
    } else {
      voteProposal.profileId = this.profile.id;
      voteProposal.proposalId = this.valueParamFollows;
      this.subscribeToSaveResponse(this.voteProposalService.create(voteProposal));
    }
  }

  private createFromForm(): IVoteProposal {
    return {
      ...new VoteProposal(),
      id: this.editForm.get(['id']).value,
      creationDate:
        this.editForm.get(['creationDate']).value != null ? moment(this.editForm.get(['creationDate']).value, DATE_TIME_FORMAT) : undefined,
      votePoints: this.editForm.get(['votePoints']).value,
      proposalId: this.editForm.get(['proposalId']).value,
      profileId: this.editForm.get(['profileId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVoteProposal>>) {
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

  trackProposalById(index: number, item: IProposal) {
    return item.id;
  }

  trackProfileById(index: number, item: IProfile) {
    return item.id;
  }
}
