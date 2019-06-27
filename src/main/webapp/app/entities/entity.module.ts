import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'post',
        loadChildren: './post/post.module#SpingularproposalPostModule'
      },
      {
        path: 'proposal',
        loadChildren: './proposal/proposal.module#SpingularproposalProposalModule'
      },
      {
        path: 'vote-proposal',
        loadChildren: './vote-proposal/vote-proposal.module#SpingularproposalVoteProposalModule'
      },
      {
        path: 'proposal-user',
        loadChildren: './proposal-user/proposal-user.module#SpingularproposalProposalUserModule'
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SpingularproposalEntityModule {}
