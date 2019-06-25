import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVoteProposal } from 'app/shared/model/vote-proposal.model';

@Component({
  selector: 'jhi-vote-proposal-detail',
  templateUrl: './vote-proposal-detail.component.html'
})
export class VoteProposalDetailComponent implements OnInit {
  voteProposal: IVoteProposal;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ voteProposal }) => {
      this.voteProposal = voteProposal;
    });
  }

  previousState() {
    window.history.back();
  }
}
