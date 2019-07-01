import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVoteProposal } from 'app/shared/model/vote-proposal.model';
import { VoteProposalService } from './vote-proposal.service';

@Component({
  selector: 'jhi-vote-proposal-delete-dialog',
  templateUrl: './vote-proposal-delete-dialog.component.html'
})
export class VoteProposalDeleteDialogComponent {
  voteProposal: IVoteProposal;

  constructor(
    protected voteProposalService: VoteProposalService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.voteProposalService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'voteProposalListModification',
        content: 'Deleted an voteProposal'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-vote-proposal-delete-popup',
  template: ''
})
export class VoteProposalDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ voteProposal }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(VoteProposalDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.voteProposal = voteProposal;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/vote-proposal', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/vote-proposal', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
