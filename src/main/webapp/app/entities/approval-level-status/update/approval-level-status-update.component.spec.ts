jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ApprovalLevelStatusService } from '../service/approval-level-status.service';
import { IApprovalLevelStatus, ApprovalLevelStatus } from '../approval-level-status.model';
import { IApprovalRequest } from 'app/entities/approval-request/approval-request.model';
import { ApprovalRequestService } from 'app/entities/approval-request/service/approval-request.service';
import { IApprover } from 'app/entities/approver/approver.model';
import { ApproverService } from 'app/entities/approver/service/approver.service';

import { ApprovalLevelStatusUpdateComponent } from './approval-level-status-update.component';

describe('Component Tests', () => {
  describe('ApprovalLevelStatus Management Update Component', () => {
    let comp: ApprovalLevelStatusUpdateComponent;
    let fixture: ComponentFixture<ApprovalLevelStatusUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let approvalLevelStatusService: ApprovalLevelStatusService;
    let approvalRequestService: ApprovalRequestService;
    let approverService: ApproverService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ApprovalLevelStatusUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ApprovalLevelStatusUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ApprovalLevelStatusUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      approvalLevelStatusService = TestBed.inject(ApprovalLevelStatusService);
      approvalRequestService = TestBed.inject(ApprovalRequestService);
      approverService = TestBed.inject(ApproverService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ApprovalRequest query and add missing value', () => {
        const approvalLevelStatus: IApprovalLevelStatus = { id: 456 };
        const request: IApprovalRequest = { id: 8360 };
        approvalLevelStatus.request = request;

        const approvalRequestCollection: IApprovalRequest[] = [{ id: 55921 }];
        spyOn(approvalRequestService, 'query').and.returnValue(of(new HttpResponse({ body: approvalRequestCollection })));
        const additionalApprovalRequests = [request];
        const expectedCollection: IApprovalRequest[] = [...additionalApprovalRequests, ...approvalRequestCollection];
        spyOn(approvalRequestService, 'addApprovalRequestToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ approvalLevelStatus });
        comp.ngOnInit();

        expect(approvalRequestService.query).toHaveBeenCalled();
        expect(approvalRequestService.addApprovalRequestToCollectionIfMissing).toHaveBeenCalledWith(
          approvalRequestCollection,
          ...additionalApprovalRequests
        );
        expect(comp.approvalRequestsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Approver query and add missing value', () => {
        const approvalLevelStatus: IApprovalLevelStatus = { id: 456 };
        const approver: IApprover = { id: 92456 };
        approvalLevelStatus.approver = approver;

        const approverCollection: IApprover[] = [{ id: 62598 }];
        spyOn(approverService, 'query').and.returnValue(of(new HttpResponse({ body: approverCollection })));
        const additionalApprovers = [approver];
        const expectedCollection: IApprover[] = [...additionalApprovers, ...approverCollection];
        spyOn(approverService, 'addApproverToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ approvalLevelStatus });
        comp.ngOnInit();

        expect(approverService.query).toHaveBeenCalled();
        expect(approverService.addApproverToCollectionIfMissing).toHaveBeenCalledWith(approverCollection, ...additionalApprovers);
        expect(comp.approversSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const approvalLevelStatus: IApprovalLevelStatus = { id: 456 };
        const request: IApprovalRequest = { id: 32371 };
        approvalLevelStatus.request = request;
        const approver: IApprover = { id: 24551 };
        approvalLevelStatus.approver = approver;

        activatedRoute.data = of({ approvalLevelStatus });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(approvalLevelStatus));
        expect(comp.approvalRequestsSharedCollection).toContain(request);
        expect(comp.approversSharedCollection).toContain(approver);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const approvalLevelStatus = { id: 123 };
        spyOn(approvalLevelStatusService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ approvalLevelStatus });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: approvalLevelStatus }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(approvalLevelStatusService.update).toHaveBeenCalledWith(approvalLevelStatus);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const approvalLevelStatus = new ApprovalLevelStatus();
        spyOn(approvalLevelStatusService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ approvalLevelStatus });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: approvalLevelStatus }));
        saveSubject.complete();

        // THEN
        expect(approvalLevelStatusService.create).toHaveBeenCalledWith(approvalLevelStatus);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const approvalLevelStatus = { id: 123 };
        spyOn(approvalLevelStatusService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ approvalLevelStatus });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(approvalLevelStatusService.update).toHaveBeenCalledWith(approvalLevelStatus);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackApprovalRequestById', () => {
        it('Should return tracked ApprovalRequest primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackApprovalRequestById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackApproverById', () => {
        it('Should return tracked Approver primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackApproverById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
