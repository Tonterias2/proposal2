import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Proposal } from 'app/shared/model/proposal.model';
import { ProposalService } from './proposal.service';
import { ProposalComponent } from './proposal.component';
import { ProposalDetailComponent } from './proposal-detail.component';
import { ProposalUpdateComponent } from './proposal-update.component';
import { ProposalDeletePopupComponent } from './proposal-delete-dialog.component';
import { IProposal } from 'app/shared/model/proposal.model';

@Injectable({ providedIn: 'root' })
export class ProposalResolve implements Resolve<IProposal> {
  constructor(private service: ProposalService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IProposal> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Proposal>) => response.ok),
        map((proposal: HttpResponse<Proposal>) => proposal.body)
      );
    }
    return of(new Proposal());
  }
}

export const proposalRoute: Routes = [
  {
    path: '',
    component: ProposalComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Proposals'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ProposalDetailComponent,
    resolve: {
      proposal: ProposalResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Proposals'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ProposalUpdateComponent,
    resolve: {
      proposal: ProposalResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Proposals'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ProposalUpdateComponent,
    resolve: {
      proposal: ProposalResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Proposals'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const proposalPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ProposalDeletePopupComponent,
    resolve: {
      proposal: ProposalResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Proposals'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
