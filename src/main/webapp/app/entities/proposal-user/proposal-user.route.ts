import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ProposalUser } from 'app/shared/model/proposal-user.model';
import { ProposalUserService } from './proposal-user.service';
import { ProposalUserComponent } from './proposal-user.component';
import { ProposalUserDetailComponent } from './proposal-user-detail.component';
import { ProposalUserUpdateComponent } from './proposal-user-update.component';
import { ProposalUserDeletePopupComponent } from './proposal-user-delete-dialog.component';
import { IProposalUser } from 'app/shared/model/proposal-user.model';

@Injectable({ providedIn: 'root' })
export class ProposalUserResolve implements Resolve<IProposalUser> {
  constructor(private service: ProposalUserService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IProposalUser> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ProposalUser>) => response.ok),
        map((proposalUser: HttpResponse<ProposalUser>) => proposalUser.body)
      );
    }
    return of(new ProposalUser());
  }
}

export const proposalUserRoute: Routes = [
  {
    path: '',
    component: ProposalUserComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'ProposalUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ProposalUserDetailComponent,
    resolve: {
      proposalUser: ProposalUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ProposalUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ProposalUserUpdateComponent,
    resolve: {
      proposalUser: ProposalUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ProposalUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ProposalUserUpdateComponent,
    resolve: {
      proposalUser: ProposalUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ProposalUsers'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const proposalUserPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ProposalUserDeletePopupComponent,
    resolve: {
      proposalUser: ProposalUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ProposalUsers'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
