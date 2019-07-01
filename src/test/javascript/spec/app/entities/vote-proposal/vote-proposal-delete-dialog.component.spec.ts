/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SpingularproposalTestModule } from '../../../test.module';
import { VoteProposalDeleteDialogComponent } from 'app/entities/vote-proposal/vote-proposal-delete-dialog.component';
import { VoteProposalService } from 'app/entities/vote-proposal/vote-proposal.service';

describe('Component Tests', () => {
  describe('VoteProposal Management Delete Component', () => {
    let comp: VoteProposalDeleteDialogComponent;
    let fixture: ComponentFixture<VoteProposalDeleteDialogComponent>;
    let service: VoteProposalService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularproposalTestModule],
        declarations: [VoteProposalDeleteDialogComponent]
      })
        .overrideTemplate(VoteProposalDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VoteProposalDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VoteProposalService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
