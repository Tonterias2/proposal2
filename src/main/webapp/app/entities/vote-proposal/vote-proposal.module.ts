import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpingularproposalSharedModule } from 'app/shared';
import {
  VoteProposalComponent,
  VoteProposalDetailComponent,
  VoteProposalUpdateComponent,
  VoteProposalDeletePopupComponent,
  VoteProposalDeleteDialogComponent,
  voteProposalRoute,
  voteProposalPopupRoute
} from './';

const ENTITY_STATES = [...voteProposalRoute, ...voteProposalPopupRoute];

@NgModule({
  imports: [SpingularproposalSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    VoteProposalComponent,
    VoteProposalDetailComponent,
    VoteProposalUpdateComponent,
    VoteProposalDeleteDialogComponent,
    VoteProposalDeletePopupComponent
  ],
  entryComponents: [
    VoteProposalComponent,
    VoteProposalUpdateComponent,
    VoteProposalDeleteDialogComponent,
    VoteProposalDeletePopupComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SpingularproposalVoteProposalModule {}
