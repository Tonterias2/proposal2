import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpingularproposalSharedModule } from 'app/shared';
import {
  ProposalUserComponent,
  ProposalUserDetailComponent,
  ProposalUserUpdateComponent,
  ProposalUserDeletePopupComponent,
  ProposalUserDeleteDialogComponent,
  proposalUserRoute,
  proposalUserPopupRoute
} from './';

const ENTITY_STATES = [...proposalUserRoute, ...proposalUserPopupRoute];

@NgModule({
  imports: [SpingularproposalSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ProposalUserComponent,
    ProposalUserDetailComponent,
    ProposalUserUpdateComponent,
    ProposalUserDeleteDialogComponent,
    ProposalUserDeletePopupComponent
  ],
  entryComponents: [
    ProposalUserComponent,
    ProposalUserUpdateComponent,
    ProposalUserDeleteDialogComponent,
    ProposalUserDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SpingularproposalProposalUserModule {}
