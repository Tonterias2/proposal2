import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { VoteProposal } from 'app/shared/model/vote-proposal.model';
import { VoteProposalService } from './vote-proposal.service';
import { VoteProposalComponent } from './vote-proposal.component';
import { VoteProposalDetailComponent } from './vote-proposal-detail.component';
import { VoteProposalUpdateComponent } from './vote-proposal-update.component';
import { VoteProposalDeletePopupComponent } from './vote-proposal-delete-dialog.component';
import { IVoteProposal } from 'app/shared/model/vote-proposal.model';

@Injectable({ providedIn: 'root' })
export class VoteProposalResolve implements Resolve<IVoteProposal> {
  constructor(private service: VoteProposalService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IVoteProposal> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<VoteProposal>) => response.ok),
        map((voteProposal: HttpResponse<VoteProposal>) => voteProposal.body)
      );
    }
    return of(new VoteProposal());
  }
}

export const voteProposalRoute: Routes = [
  {
    path: '',
    component: VoteProposalComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'VoteProposals'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: VoteProposalDetailComponent,
    resolve: {
      voteProposal: VoteProposalResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'VoteProposals'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: VoteProposalUpdateComponent,
    resolve: {
      voteProposal: VoteProposalResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'VoteProposals'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: VoteProposalUpdateComponent,
    resolve: {
      voteProposal: VoteProposalResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'VoteProposals'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const voteProposalPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: VoteProposalDeletePopupComponent,
    resolve: {
      voteProposal: VoteProposalResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'VoteProposals'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
