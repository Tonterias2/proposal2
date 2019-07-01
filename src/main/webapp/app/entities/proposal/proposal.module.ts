import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpingularproposalSharedModule } from 'app/shared';
import {
  ProposalComponent,
  ProposalDetailComponent,
  ProposalUpdateComponent,
  ProposalDeletePopupComponent,
  ProposalDeleteDialogComponent,
  proposalRoute,
  proposalPopupRoute
} from './';

const ENTITY_STATES = [...proposalRoute, ...proposalPopupRoute];

@NgModule({
  imports: [SpingularproposalSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ProposalComponent,
    ProposalDetailComponent,
    ProposalUpdateComponent,
    ProposalDeleteDialogComponent,
    ProposalDeletePopupComponent
  ],
  entryComponents: [ProposalComponent, ProposalUpdateComponent, ProposalDeleteDialogComponent, ProposalDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SpingularproposalProposalModule {}
