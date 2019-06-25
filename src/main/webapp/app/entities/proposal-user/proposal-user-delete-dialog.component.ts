import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProposalUser } from 'app/shared/model/proposal-user.model';
import { ProposalUserService } from './proposal-user.service';

@Component({
  selector: 'jhi-proposal-user-delete-dialog',
  templateUrl: './proposal-user-delete-dialog.component.html'
})
export class ProposalUserDeleteDialogComponent {
  proposalUser: IProposalUser;

  constructor(
    protected proposalUserService: ProposalUserService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.proposalUserService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'proposalUserListModification',
        content: 'Deleted an proposalUser'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-proposal-user-delete-popup',
  template: ''
})
export class ProposalUserDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ proposalUser }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ProposalUserDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.proposalUser = proposalUser;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/proposal-user', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/proposal-user', { outlets: { popup: null } }]);
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
