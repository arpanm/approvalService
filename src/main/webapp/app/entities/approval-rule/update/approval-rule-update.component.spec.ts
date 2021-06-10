jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ApprovalRuleService } from '../service/approval-rule.service';
import { IApprovalRule, ApprovalRule } from '../approval-rule.model';

import { ApprovalRuleUpdateComponent } from './approval-rule-update.component';

describe('Component Tests', () => {
  describe('ApprovalRule Management Update Component', () => {
    let comp: ApprovalRuleUpdateComponent;
    let fixture: ComponentFixture<ApprovalRuleUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let approvalRuleService: ApprovalRuleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ApprovalRuleUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ApprovalRuleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ApprovalRuleUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      approvalRuleService = TestBed.inject(ApprovalRuleService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const approvalRule: IApprovalRule = { id: 456 };

        activatedRoute.data = of({ approvalRule });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(approvalRule));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const approvalRule = { id: 123 };
        spyOn(approvalRuleService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ approvalRule });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: approvalRule }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(approvalRuleService.update).toHaveBeenCalledWith(approvalRule);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const approvalRule = new ApprovalRule();
        spyOn(approvalRuleService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ approvalRule });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: approvalRule }));
        saveSubject.complete();

        // THEN
        expect(approvalRuleService.create).toHaveBeenCalledWith(approvalRule);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const approvalRule = { id: 123 };
        spyOn(approvalRuleService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ approvalRule });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(approvalRuleService.update).toHaveBeenCalledWith(approvalRule);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
