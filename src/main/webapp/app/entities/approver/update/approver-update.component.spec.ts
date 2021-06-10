jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ApproverService } from '../service/approver.service';
import { IApprover, Approver } from '../approver.model';
import { IApprovalRule } from 'app/entities/approval-rule/approval-rule.model';
import { ApprovalRuleService } from 'app/entities/approval-rule/service/approval-rule.service';

import { ApproverUpdateComponent } from './approver-update.component';

describe('Component Tests', () => {
  describe('Approver Management Update Component', () => {
    let comp: ApproverUpdateComponent;
    let fixture: ComponentFixture<ApproverUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let approverService: ApproverService;
    let approvalRuleService: ApprovalRuleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ApproverUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ApproverUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ApproverUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      approverService = TestBed.inject(ApproverService);
      approvalRuleService = TestBed.inject(ApprovalRuleService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ApprovalRule query and add missing value', () => {
        const approver: IApprover = { id: 456 };
        const rule: IApprovalRule = { id: 77210 };
        approver.rule = rule;

        const approvalRuleCollection: IApprovalRule[] = [{ id: 82059 }];
        spyOn(approvalRuleService, 'query').and.returnValue(of(new HttpResponse({ body: approvalRuleCollection })));
        const additionalApprovalRules = [rule];
        const expectedCollection: IApprovalRule[] = [...additionalApprovalRules, ...approvalRuleCollection];
        spyOn(approvalRuleService, 'addApprovalRuleToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ approver });
        comp.ngOnInit();

        expect(approvalRuleService.query).toHaveBeenCalled();
        expect(approvalRuleService.addApprovalRuleToCollectionIfMissing).toHaveBeenCalledWith(
          approvalRuleCollection,
          ...additionalApprovalRules
        );
        expect(comp.approvalRulesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const approver: IApprover = { id: 456 };
        const rule: IApprovalRule = { id: 81920 };
        approver.rule = rule;

        activatedRoute.data = of({ approver });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(approver));
        expect(comp.approvalRulesSharedCollection).toContain(rule);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const approver = { id: 123 };
        spyOn(approverService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ approver });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: approver }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(approverService.update).toHaveBeenCalledWith(approver);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const approver = new Approver();
        spyOn(approverService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ approver });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: approver }));
        saveSubject.complete();

        // THEN
        expect(approverService.create).toHaveBeenCalledWith(approver);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const approver = { id: 123 };
        spyOn(approverService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ approver });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(approverService.update).toHaveBeenCalledWith(approver);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackApprovalRuleById', () => {
        it('Should return tracked ApprovalRule primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackApprovalRuleById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
