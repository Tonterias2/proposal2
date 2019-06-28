import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProposalUser } from 'app/shared/model/proposal-user.model';

@Component({
  selector: 'jhi-proposal-user-detail',
  templateUrl: './proposal-user-detail.component.html'
})
export class ProposalUserDetailComponent implements OnInit {
  proposalUser: IProposalUser;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ proposalUser }) => {
      this.proposalUser = proposalUser;
    });
  }

  previousState() {
    window.history.back();
  }
}
