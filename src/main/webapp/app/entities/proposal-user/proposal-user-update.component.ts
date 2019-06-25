import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IProposalUser, ProposalUser } from 'app/shared/model/proposal-user.model';
import { ProposalUserService } from './proposal-user.service';
import { IUser, UserService } from 'app/core';

@Component({
  selector: 'jhi-proposal-user-update',
  templateUrl: './proposal-user-update.component.html'
})
export class ProposalUserUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    assignedVotesPoints: [],
    userId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected proposalUserService: ProposalUserService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ proposalUser }) => {
      this.updateForm(proposalUser);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(proposalUser: IProposalUser) {
    this.editForm.patchValue({
      id: proposalUser.id,
      creationDate: proposalUser.creationDate != null ? proposalUser.creationDate.format(DATE_TIME_FORMAT) : null,
      assignedVotesPoints: proposalUser.assignedVotesPoints,
      userId: proposalUser.userId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const proposalUser = this.createFromForm();
    if (proposalUser.id !== undefined) {
      this.subscribeToSaveResponse(this.proposalUserService.update(proposalUser));
    } else {
      this.subscribeToSaveResponse(this.proposalUserService.create(proposalUser));
    }
  }

  private createFromForm(): IProposalUser {
    return {
      ...new ProposalUser(),
      id: this.editForm.get(['id']).value,
      creationDate:
        this.editForm.get(['creationDate']).value != null ? moment(this.editForm.get(['creationDate']).value, DATE_TIME_FORMAT) : undefined,
      assignedVotesPoints: this.editForm.get(['assignedVotesPoints']).value,
      userId: this.editForm.get(['userId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProposalUser>>) {
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

  trackUserById(index: number, item: IUser) {
    return item.id;
  }
}
