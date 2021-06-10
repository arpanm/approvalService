jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ApprovalRequestService } from '../service/approval-request.service';
import { IApprovalRequest, ApprovalRequest } from '../approval-request.model';
import { IApprovalRule } from 'app/entities/approval-rule/approval-rule.model';
import { ApprovalRuleService } from 'app/entities/approval-rule/service/approval-rule.service';

import { ApprovalRequestUpdateComponent } from './approval-request-update.component';

describe('Component Tests', () => {
  describe('ApprovalRequest Management Update Component', () => {
    let comp: ApprovalRequestUpdateComponent;
    let fixture: ComponentFixture<ApprovalRequestUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let approvalRequestService: ApprovalRequestService;
    let approvalRuleService: ApprovalRuleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ApprovalRequestUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ApprovalRequestUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ApprovalRequestUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      approvalRequestService = TestBed.inject(ApprovalRequestService);
      approvalRuleService = TestBed.inject(ApprovalRuleService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ApprovalRule query and add missing value', () => {
        const approvalRequest: IApprovalRequest = { id: 456 };
        const rule: IApprovalRule = { id: 22320 };
        approvalRequest.rule = rule;

        const approvalRuleCollection: IApprovalRule[] = [{ id: 38862 }];
        spyOn(approvalRuleService, 'query').and.returnValue(of(new HttpResponse({ body: approvalRuleCollection })));
        const additionalApprovalRules = [rule];
        const expectedCollection: IApprovalRule[] = [...additionalApprovalRules, ...approvalRuleCollection];
        spyOn(approvalRuleService, 'addApprovalRuleToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ approvalRequest });
        comp.ngOnInit();

        expect(approvalRuleService.query).toHaveBeenCalled();
        expect(approvalRuleService.addApprovalRuleToCollectionIfMissing).toHaveBeenCalledWith(
          approvalRuleCollection,
          ...additionalApprovalRules
        );
        expect(comp.approvalRulesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const approvalRequest: IApprovalRequest = { id: 456 };
        const rule: IApprovalRule = { id: 64713 };
        approvalRequest.rule = rule;

        activatedRoute.data = of({ approvalRequest });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(approvalRequest));
        expect(comp.approvalRulesSharedCollection).toContain(rule);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const approvalRequest = { id: 123 };
        spyOn(approvalRequestService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ approvalRequest });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: approvalRequest }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(approvalRequestService.update).toHaveBeenCalledWith(approvalRequest);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const approvalRequest = new ApprovalRequest();
        spyOn(approvalRequestService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ approvalRequest });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: approvalRequest }));
        saveSubject.complete();

        // THEN
        expect(approvalRequestService.create).toHaveBeenCalledWith(approvalRequest);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const approvalRequest = { id: 123 };
        spyOn(approvalRequestService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ approvalRequest });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(approvalRequestService.update).toHaveBeenCalledWith(approvalRequest);
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
