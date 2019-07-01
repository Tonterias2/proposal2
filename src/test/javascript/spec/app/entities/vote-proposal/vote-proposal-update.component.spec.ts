/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { SpingularproposalTestModule } from '../../../test.module';
import { VoteProposalUpdateComponent } from 'app/entities/vote-proposal/vote-proposal-update.component';
import { VoteProposalService } from 'app/entities/vote-proposal/vote-proposal.service';
import { VoteProposal } from 'app/shared/model/vote-proposal.model';

describe('Component Tests', () => {
  describe('VoteProposal Management Update Component', () => {
    let comp: VoteProposalUpdateComponent;
    let fixture: ComponentFixture<VoteProposalUpdateComponent>;
    let service: VoteProposalService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularproposalTestModule],
        declarations: [VoteProposalUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(VoteProposalUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VoteProposalUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VoteProposalService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new VoteProposal(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new VoteProposal();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
