/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { SpingularproposalTestModule } from '../../../test.module';
import { ProposalUserUpdateComponent } from 'app/entities/proposal-user/proposal-user-update.component';
import { ProposalUserService } from 'app/entities/proposal-user/proposal-user.service';
import { ProposalUser } from 'app/shared/model/proposal-user.model';

describe('Component Tests', () => {
  describe('ProposalUser Management Update Component', () => {
    let comp: ProposalUserUpdateComponent;
    let fixture: ComponentFixture<ProposalUserUpdateComponent>;
    let service: ProposalUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularproposalTestModule],
        declarations: [ProposalUserUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ProposalUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProposalUserUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProposalUserService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ProposalUser(123);
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
        const entity = new ProposalUser();
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
