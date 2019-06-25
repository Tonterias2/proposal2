/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SpingularproposalTestModule } from '../../../test.module';
import { ProposalUserDeleteDialogComponent } from 'app/entities/proposal-user/proposal-user-delete-dialog.component';
import { ProposalUserService } from 'app/entities/proposal-user/proposal-user.service';

describe('Component Tests', () => {
  describe('ProposalUser Management Delete Component', () => {
    let comp: ProposalUserDeleteDialogComponent;
    let fixture: ComponentFixture<ProposalUserDeleteDialogComponent>;
    let service: ProposalUserService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularproposalTestModule],
        declarations: [ProposalUserDeleteDialogComponent]
      })
        .overrideTemplate(ProposalUserDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProposalUserDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProposalUserService);
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
